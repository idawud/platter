package io.turntabl.platterworker.services

import java.time.LocalDateTime

import com.google.gson.JsonObject
import io.turntabl.platterworker.models.{Forecast, StationForecast, StationInformation, WeatherData}

import scala.collection.mutable.ListBuffer

class WeatherDataFetching {

  def dataFromStation(): List[StationInformation] = {
    val elements = Utils.fetchDataFromApiEndpoint("wxobs/all/json/sitelist")
    val locationJson = elements.getAsJsonObject.get("Locations").getAsJsonObject.get("Location").getAsJsonArray
    Utils.jsonArrayToListStation(locationJson)
  }

  def getTimeStamps() = {
    val elements = Utils.fetchDataFromApiEndpoint("wxfcs/all/json/capabilities")
    val locationJson = elements.getAsJsonObject.get("Resource").getAsJsonObject.get("TimeSteps").getAsJsonObject.get("TS").getAsJsonArray
    val dateTime = elements.getAsJsonObject.get("Resource").getAsJsonObject.get("dataDate").toString.replace("\"", "")
    val timeStamps = Utils.jsonArrayToStringList(locationJson)
    timeStampToConsider(dateTime.replace("Z", ""), timeStamps)
  }


  def forecastFromAllStations(): List[StationForecast] = {
    val dfs = dataFromStation()
    val stationInformation =  dfs map( x => forecastFromSingleStation(x.locationId))
    val data = dfs zip(stationInformation)
    data map StationForecastCreator
  }

  def StationForecastCreator( data: (StationInformation, JsonObject)): StationForecast = {
    val stationInformation = data._1
    val others = data._2
    val dateTime = others.get("dataDate").getAsString
    val country = others.get("Location").getAsJsonObject.get("country").getAsString
    val periods = others.get("Location").getAsJsonObject.get("Period").getAsJsonArray.get(1).getAsJsonObject.get("Rep").getAsJsonArray
    val listBuffer: ListBuffer[WeatherData] = ListBuffer()

    var i = 0
    while ( i < periods.size()){
      val p = periods.get(i).getAsJsonObject
      val item = WeatherData(p.get("T").getAsString, p.get("H").getAsString, p.get("V").getAsString, p.get("W").getAsString, p.get("U").getAsString,
        p.get("F").getAsString, p.get("D").getAsString, p.get("G").getAsString, p.get("Pp").getAsString, p.get("S").getAsString)
      listBuffer.append(item)
      i += 1
    }
    val forecasts = getTimeStamps() zip listBuffer map(x => Forecast(x._1, x._2))
    StationForecast(stationInformation, country, dateTime, forecasts)
  }

  def forecastFromSingleStation(locationId: String): JsonObject = {
    val element = Utils.fetchDataFromApiEndpoint("wxfcs/all/json/" + locationId)
    element.getAsJsonObject.get("SiteRep").getAsJsonObject.get("DV").getAsJsonObject
  }

  private def timeStampToConsider(today: String, timestampIntervals: List[String]) = {
    val time = LocalDateTime.parse(today).plusDays(1)
    val x = time.toString.split('T')
    val y = x.head
    timestampIntervals.slice(timestampIntervals.indexWhere(p => p.startsWith(y)), timestampIntervals.lastIndexWhere(p => p.startsWith(y)) + 1)
  }

}

