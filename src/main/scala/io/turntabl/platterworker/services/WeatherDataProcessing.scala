package io.turntabl.platterworker.services

import com.google.gson.{JsonArray, JsonObject}
import io.turntabl.platterworker.models.{Forecast, StationForecast, StationInformation, WeatherData}

class WeatherDataProcessing {
  private def parseToDouble(num: String): Double = try  java.lang.Double.valueOf(num) catch {case e: Exception => 0.0}

  def countyInfoToJsonString(stationForecast: StationForecast): (String, JsonObject) = {
    val country:String =  stationForecast.country
    val county:String = stationForecast.information.unitaryAuthArea
    val information = stationInformationJson(stationForecast.information)
    val forecast = stationForecast.forecast map forecastJson
    val forecastObj = new JsonArray()

    forecast foreach(x => forecastObj.add(x))

    information.add("periods", forecastObj)
    information.addProperty("dataDate", stationForecast.dataDate)
    information.addProperty("country", stationForecast.country)

    val summary: JsonObject = getAveragesAsJson(stationForecast)

    information.add("summary", summary)
    (s"$country/$county/", information)
  }


  private def getAveragesAsJson(stationForecast: StationForecast): JsonObject = {
    val (temp: Double, humidity: Double, windSpeed: Double) = getAverages(stationForecast)

    val summary = new JsonObject
    summary.addProperty("temp", temp)
    summary.addProperty("humidity", humidity)
    summary.addProperty("windSpeed", windSpeed)
    summary
  }

  def getAverages(stationForecast: StationForecast): (Double, Double, Double) = {
    val temp: Double = (stationForecast.forecast map (x => parseToDouble(x.data.temperature))).sum / 8
    val humidity: Double = (stationForecast.forecast map (x => parseToDouble(x.data.humidity))).sum / 8
    val windSpeed: Double = (stationForecast.forecast map (x => parseToDouble(x.data.windSpeed))).sum / 8
    (temp, humidity, windSpeed)
  }

  def forecastJson(forecast: Forecast): JsonObject = {
    val obj = new JsonObject()
    obj.addProperty("timestamp", forecast.timeStamp)
    val period = new JsonObject
    period.addProperty("temperature", forecast.data.temperature)
    period.addProperty("feelsGoodTemp", forecast.data.feelsGoodTemp)
    period.addProperty("humidity", forecast.data.humidity)
    period.addProperty("MaxUVIndex", forecast.data.MaxUVIndex)
    period.addProperty("precipitationProb", forecast.data.precipitationProb)
    period.addProperty("visibility", forecast.data.visibility)
    period.addProperty("weatherType", forecast.data.weatherType)
    period.addProperty("windDirection", forecast.data.windDirection)
    period.addProperty("windGust", forecast.data.windGust)
    period.addProperty("windSpeed", forecast.data.windSpeed)
    obj.add("reading", period)
    obj
  }

  def stationInformationJson(info: StationInformation): JsonObject = {
    val obj = new JsonObject()
    obj.addProperty("elevation", info.elevation)
    obj.addProperty("locationId", info.locationId)
    obj.addProperty("latitude", info.latitude)
    obj.addProperty("longitude", info.longitude)
    obj.addProperty("name", info.name)
    obj.addProperty("county", info.unitaryAuthArea)
    obj
  }
}
