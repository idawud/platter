package io.turntabl.platterworker.services

import java.nio.file.{Files, Path, Paths}
import java.time.LocalDateTime

import com.google.gson.JsonObject
import io.turntabl.platterworker.AWS.CloudStorage
import io.turntabl.platterworker.models.{Forecast, StationForecast, StationInformation, WeatherData}

object ServiceRunner {
  private val dataProcessing = new WeatherDataProcessing
  private val fetchData = new WeatherDataFetching

   def run(): Unit = {
     val data: List[StationForecast] = fetchData.forecastFromAllStations()
     rawData(data)
     aggregation(data)
   }

  private def rawData(stationData : List[StationForecast]): Unit = {
    val data: List[(String, JsonObject)] = stationData.map(x => dataProcessing.countyInfoToJsonString(x))
    val timeStamp = s"${LocalDateTime.now().withNano(0).withHour(0).withMinute(0).withSecond(0).plusDays(1)}"
    val path: Path = Files.createFile(Paths.get(s"${timeStamp}.json"))
    data foreach (x => writeAndUpload(x, timeStamp, path))
    Files.delete(path)
  }

  def aggregation(data : List[StationForecast] ): Unit = {
    val ukAvg: (Double, Double, Double) = allAverages( data map  dataProcessing.getAverages)
    val sortedByCountry = data groupBy(k => k.country)
    val countryAvg: Map[String, (Double, Double, Double)] = sortedByCountry map (v => (v._1, allAverages(v._2 map dataProcessing.getAverages)))
    val countyAvg: Map[String, Map[String, (Double, Double, Double)]] = sortedByCountry map (c => (c._1, c._2.groupBy(t => t.information.unitaryAuthArea) map(r => (r._1, allAverages(r._2 map dataProcessing.getAverages )))))

    val timeStamp = s"${LocalDateTime.now().withNano(0).withHour(0).withMinute(0).withSecond(0).plusDays(1)}"
    val path: Path = Files.createFile(Paths.get(s"${timeStamp}.json"))

    uploadUKAverage(ukAvg, timeStamp, path)

    countryAvg.keySet foreach ( x => {
      Files.write(path, tuple3DoubleToObject(countryAvg(x), x).toString.getBytes())
      CloudStorage.upload(timeStamp, s"summary/${x}/", path)
    })

    countyAvg.keySet foreach ( x => {
      countyAvg(x) foreach( y => {
        Files.write(path, tuple3DoubleToObject(countyAvg(x)(y._1), y._1).toString.getBytes())
        CloudStorage.upload(timeStamp, s"summary/${x}/${y._1}/", path)
      })})

    Files.delete(path)
  }

  private def uploadUKAverage(ukAvg: (Double, Double, Double), timeStamp: String, path: Path) = {
    Files.write(path, tuple3DoubleToObject(ukAvg, "UK").toString.getBytes())
    CloudStorage.upload(timeStamp, "summary/UK/", path)
  }

  def tuple3DoubleToObject(d: (Double, Double, Double), place: String ): JsonObject = {
    val summary = new JsonObject
    summary.addProperty("desc", s"Average of Whole of the ${place}")
    summary.addProperty("temp", d._1)
    summary.addProperty("humidity", d._2)
    summary.addProperty("windSpeed", d._3)
    summary
  }

  private def allAverages( value : List[(Double, Double, Double)]): (Double, Double, Double) = {
    val length = value.size
    val temp = (value map(x => x._1) sum) / length
    val humidity = (value map(x => x._2) sum) / length
    val windSpeed = (value map(x => x._3) sum) / length
    (temp, humidity, windSpeed)
  }

  private def writeAndUpload(data: (String, JsonObject), timeStamp: String, path: Path) = {
    Files.write(path, data._2.toString.getBytes())
    CloudStorage.upload(timeStamp, data._1, path)
  }

}
