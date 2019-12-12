package io.turntabl.platterworker.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.{Gson, JsonArray, JsonObject}
import io.turntabl.platterworker.models.{Forecast, StationForecast, StationInformation, WeatherData}

class WeatherDataProcessing {
  private val weatherData = new WeatherDataFetching

  def grouping() = {
    val data = List( StationForecast(StationInformation("23.0", "14", "656565", "858858", "london", "morray"), "ENGLAND", "2019-12-23T12:00:00Z",
      List(Forecast("2019-12-23T12:00:00Z", WeatherData("3","3","3","3","3","3","3","3","3","3")),
        Forecast("2019-12-23T12:00:00Z", WeatherData("3","3","3","3","3","3","3","3","3","3")))),

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

  def StationForecastJson(station: StationForecast, countriesObj: JsonObject) = {

    val information = stationInformationJson(station.information)
    val forecast = station.forecast map forecastJson
    val forecastObj = new JsonArray()
    forecast foreach(x => forecastObj.add(x))

    information.add("periods", forecastObj)
    information.addProperty("dataDate", station.dataDate)

    if ( countriesObj.keySet().contains(station.country)){
      val initObj = new JsonObject
      initObj.add(station.information.unitaryAuthArea, information)
      countriesObj.get(station.country).getAsJsonArray.add(initObj)
    }else{
       val initObj = new JsonObject
      initObj.add(station.information.unitaryAuthArea, information)
      val y = new JsonArray()
        y.add(initObj)
      countriesObj.add(station.country, y)
    }
  }



  def toJsonString() = {
    val data = grouping()
    val countriesObj = new JsonObject
    data foreach(x => StationForecastJson(x, countriesObj))
    countriesObj
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
    //obj.addProperty("unitaryAuthArea", info.unitaryAuthArea)
    obj
  }
}
