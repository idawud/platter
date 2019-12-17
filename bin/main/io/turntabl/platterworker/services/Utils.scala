package io.turntabl.platterworker.services

import java.net.URI
import java.net.http.{HttpClient, HttpRequest, HttpResponse}
import java.util

import scala.collection.mutable.ListBuffer
import com.google.gson.{JsonArray, JsonElement, JsonParser}
import io.turntabl.platterworker.models.StationInformation

object Utils {
    private val API_BASE: String = "http://datapoint.metoffice.gov.uk/public/data/val/"
    private val API_KEY: String = "?res=3hourly&key=ca3ee938-3ade-405e-adc1-38990741404b"

    def fetchDataFromApiEndpoint(urlSectionPart: String): JsonElement = {
      val request = HttpRequest.newBuilder().uri(new URI(API_BASE + urlSectionPart + API_KEY)).build()
      val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString)
      response.body()
       (new JsonParser).parse(response.body())
    }

  def jsonArrayToListTimestamp(elements: JsonArray): List[String] =
  {
    val items: ListBuffer[String] = ListBuffer()
    val iter = elements.iterator();
    while (iter.hasNext) {
      val current = iter.next()
      if (!current.isJsonNull){
        items.append(current.getAsString)
      }
    }
  items.toList
  }

  def jsonArrayToListStation(elements: JsonArray): List[StationInformation] = {
    val items: ListBuffer[StationInformation] = ListBuffer()
    val iter = elements.iterator();
    while (iter.hasNext){
        val  current = iter.next()
        val keys = current.getAsJsonObject.keySet()
        val obj = createObjFromElementLocation(current, keys)
        obj match {
          case x: StationInformation => items.append(x)
          case _ =>
       }
    }
    items.toList
  }

  private def createObjFromElementLocation(current: JsonElement, keys: util.Set[String]) = {
    if (!current.isJsonNull && keys.contains("region") && keys.contains("unitaryAuthArea") && keys.contains("name")) {
      StationInformation(
        current.getAsJsonObject.get("elevation").getAsString,
        current.getAsJsonObject.get("id").getAsString,
        current.getAsJsonObject.get("latitude").getAsString,
        current.getAsJsonObject.get("longitude").getAsString,
        current.getAsJsonObject.get("name").getAsString,
        current.getAsJsonObject.get("unitaryAuthArea").getAsString
      )
    }
  }

  def jsonArrayToStringList(element: JsonArray): List[String] = {
    val listBuffer: ListBuffer[String] = ListBuffer()
    var i = 0
    while ( i < element.size()) {
      val item = element.get(i).getAsString
      listBuffer.append(item)
      i += 1
    }
    listBuffer.toList
  }

}
