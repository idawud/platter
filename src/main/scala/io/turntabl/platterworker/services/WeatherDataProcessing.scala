package io.turntabl.platterworker.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.{Gson, JsonObject}
import io.turntabl.platterworker.models.StationForecast

class WeatherDataProcessing {
  private val weatherData = new WeatherDataFetching

  def grouping() = {
    val data = weatherData.forecastFromAllStations()
    val byCountry = data groupBy(f => f.country) toMap()
    byCountry
  }
  def toJsonString() = {
    val data = grouping()
    //val dat = (new Gson()).toJson(data)
    //dat
    println(data)
    (new ObjectMapper).writeValueAsString(data)
  }
}
