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
     // val data: List[StationForecast] = grouping()
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
    val ukAvg = allAverages( data map  dataProcessing.getAverages)
    val sortedByCountry = data groupBy(k => k.country)
    val countryAvg = sortedByCountry map ( v => (v._1, allAverages(v._2 map dataProcessing.getAverages)))
    val countyAvg = sortedByCountry map ( c => (c._1, c._2.groupBy(t => t.information.unitaryAuthArea) map( r => (r._1, allAverages(r._2 map dataProcessing.getAverages )))))

    println(ukAvg)
    println(countryAvg)
    println(countyAvg)
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

  def grouping(): List[StationForecast] = {
    val data = List( StationForecast(StationInformation("23.0", "14", "656565", "858858", "london", "morray"), "ENGLAND", "2019-12-23T12:00:00Z",
      List(Forecast("2019-12-23T12:00:00Z", WeatherData("8","3","3","3","3","3","3","3","3","3")),
        Forecast("2019-12-23T12:00:00Z", WeatherData("8","3","3","3","3","3","3","3","3","3")))),

      StationForecast(StationInformation("23.0", "14", "656565", "858858", "london", "Accra"), "Ghana", "2019-12-23T12:00:00Z",
        List(Forecast("2019-12-23T12:00:00Z", WeatherData("3","3","3","3","3","3","3","3","3","3")),
          Forecast("2019-12-23T12:00:00Z", WeatherData("7","3","3","3","3","3","3","3","3","3")))),

      StationForecast(StationInformation("23.0", "14", "656565", "858858", "london", "surrey"), "Scotland", "2019-12-23T12:00:00Z",
        List(Forecast("2019-12-23T12:00:00Z", WeatherData("12","3","3","3","3","3","3","3","3","3")),
          Forecast("2019-12-23T12:00:00Z", WeatherData("3","3","3","3","3","3","3","3","3","3")))),

      StationForecast(StationInformation("23.0", "14", "656565", "858858", "london", "manchester"), "ENGLAND", "2019-12-23T12:00:00Z",
        List(Forecast("2019-12-23T12:00:00Z", WeatherData("3","3","3","3","3","3","3","3","3","3")),
          Forecast("2019-12-23T12:00:00Z", WeatherData("3","3","3","3","3","3","3","3","3","3")))),

      StationForecast(StationInformation("23.0", "14", "656565", "858858", "london", "greater-london"), "ENGLAND", "2019-12-23T12:00:00Z",
        List(Forecast("2019-12-23T12:00:00Z", WeatherData("3","3","3","3","3","3","3","3","3","3")),
          Forecast("2019-12-23T12:00:00Z", WeatherData("3","3","3","3","3","3","3","3","3","3"))))
    )
    data
  }
}
