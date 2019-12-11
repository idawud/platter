package io.turntabl.platterworker.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.{Gson, JsonObject}
import io.turntabl.platterworker.models.StationForecast

class WeatherDataProcessing {
  private val weatherData = new WeatherDataFetching

  def grouping() = {
    val data = weatherData.forecastFromAllStations()
    println(data)
   // val byCountry = data groupBy(f => f.country)
   // byCountry.keys map( x => byCountry.get(x).groupBy(f => f.groupBy(g => g.information.unitaryAuthArea)))
  }

  def toJsonString() = {
    val data = grouping()
    //val dat = (new Gson()).toJson(data)
    //dat
   // val records = data map( x =>  (new ObjectMapper).writeValueAsString(x))
   // records foreach println
  }
}
