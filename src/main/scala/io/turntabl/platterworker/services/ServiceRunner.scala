package io.turntabl.platterworker.services

import java.nio.file.{Files, Path, Paths}
import java.time.LocalDateTime

import com.google.gson.JsonObject
import io.turntabl.platterworker.AWS.CloudStorage
import io.turntabl.platterworker.models.{Forecast, StationForecast, StationInformation, WeatherData}

object ServiceRunner {
   // private val fetchedData = (new WeatherDataFetching).forecastFromAllStations()

   def run(): Unit = {
    val wp = new WeatherDataProcessing()
     // val data: List[(String, JsonObject)] = fetchedData map wp.countyInfoToJsonString
    val data: List[(String, JsonObject)] = grouping() map wp.countyInfoToJsonString

    val timeStamp = s"${LocalDateTime.now().withNano(0).withHour(0).withMinute(0).withSecond(0)}"
    val path: Path = Files.createFile(Paths.get(s"${timeStamp}.json"))

    data foreach ( x => writeAndUpload(x, timeStamp, path))
    Files.delete(path)
  }

  def aggregation(): Unit = {
    val data = grouping()
    val result = data.map( x => x.forecast.map(x => parseToDouble(x.data.temperature)) )
    // println(dayByDay(result))
    println(result)
  }

  // def dayByDay(data: List[List[Double]]) = for ( )

  private def parseToDouble(num: String): Double = try  java.lang.Double.valueOf(num) catch {case e: Exception => 0.0}

  private def writeAndUpload(data: (String, JsonObject), timeStamp: String, path: Path) = {
    Files.write(path, data._2.toString.getBytes())
    CloudStorage.upload(timeStamp, data._1, path)
  }



  def grouping(): List[StationForecast] = {
    val data = List( StationForecast(StationInformation("23.0", "14", "656565", "858858", "london", "morray"), "New ENGLAND", "2019-12-23T12:00:00Z",
      List(Forecast("2019-12-23T12:00:00Z", WeatherData("8","3","3","3","3","3","3","3","3","3")),
        Forecast("2019-12-23T12:00:00Z", WeatherData("8","3","3","3","3","3","3","3","3","3")))),

      StationForecast(StationInformation("23.0", "14", "656565", "858858", "london", "Accra"), "Ghana", "2019-12-23T12:00:00Z",
        List(Forecast("2019-12-23T12:00:00Z", WeatherData("3","3","3","3","3","3","3","3","3","3")),
          Forecast("2019-12-23T12:00:00Z", WeatherData("3","3","3","3","3","3","3","3","3","3")))),

      StationForecast(StationInformation("23.0", "14", "656565", "858858", "london", "surrey"), "Scotland", "2019-12-23T12:00:00Z",
        List(Forecast("2019-12-23T12:00:00Z", WeatherData("3","3","3","3","3","3","3","3","3","3")),
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
