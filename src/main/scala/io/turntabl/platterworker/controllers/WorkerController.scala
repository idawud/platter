package io.turntabl.platterworker.controllers


import java.time.LocalDateTime

import com.google.gson.{JsonArray, JsonObject, JsonParser}
import io.turntabl.platterworker.AWS.CloudStorage
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation._

import scala.collection.mutable.ListBuffer


@RestController
@CrossOrigin(origins = Array("*"))
@RequestMapping(path = Array("/api/v1"))
class WorkerController() {
  private def getPlaces = {
    val parser = new JsonParser
    val content: String = CloudStorage.contentOfObject("register/places_register.json")
    val jobber = parser.parse(content)
    val places = jobber.getAsJsonObject.keySet()
    places
  }


  @RequestMapping(path = Array("/search"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def search(@RequestParam("name") name: String): JsonObject = {
    val  places = getPlaces
    val arr = new JsonArray()

    val iter = places.iterator()
    while ( iter.hasNext ) {
      val value = iter.next()
      if ( value.toLowerCase.contains(name.trim.toLowerCase)){ arr.add(value) }
    }

    val obj = new JsonObject
    obj.add("places", arr)
    obj
  }

  @RequestMapping(path = Array("/places"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def places(): JsonObject = {
    val  places = getPlaces
    val arr = new JsonArray
    val iter = places.iterator()
    while ( iter.hasNext ) { arr.add(iter.next())}
    val obj = new JsonObject
    obj.add("places", arr)
    obj
  }

  @RequestMapping(path = Array("/place"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def placeData(@RequestParam("name") name: String): JsonObject =  {
    val parser = new JsonParser
    val date: String = s"${LocalDateTime.now().withNano(0).withHour(0).withMinute(0).withSecond(0).plusDays(1)}"

    val places: String = CloudStorage.contentOfObject("register/places_register.json")
    val jobberz = parser.parse(places)

    if ( jobberz.getAsJsonObject.keySet().contains(name.trim)) {
      val route: String = jobberz.getAsJsonObject.get(name.trim).getAsString
      val contents: String = CloudStorage.contentOfObject(s"${route}${date}${name.trim}.json")
      val jobber = parser.parse(contents)
      return jobber.getAsJsonObject
    }

    val defaultObj = new JsonObject
    defaultObj.addProperty("error", "PlaceNotFound")
    defaultObj
  }

}
