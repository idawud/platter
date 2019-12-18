package io.turntabl.platterworker.controllers


import java.time.LocalDateTime

import com.google.gson.{JsonArray, JsonObject, JsonParser}
import io.turntabl.platterworker.AWS.CloudStorage
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation._


@RestController
@CrossOrigin(origins = Array("*"))
@RequestMapping(path = Array("/api/v1"))
class WorkerController() {
  private def getPlaces(category: String) = {
    val parser = new JsonParser
    val content: String = CloudStorage.contentOfObject(s"register/${category}_register.json")
    val jobber = parser.parse(content)
    val places = jobber.getAsJsonObject.keySet()
    places
  }


  @RequestMapping(path = Array("/summary"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def summary(): JsonObject = {
    val date: String = s"${LocalDateTime.now().withNano(0).withHour(0).withMinute(0).withSecond(0)}"
    val parser = new JsonParser
    val uk = parser.parse(CloudStorage.contentOfObject(s"summary/UK/${date}.json"))
    val wales = parser.parse(CloudStorage.contentOfObject(s"summary/WALES/${date}.json"))
    val scotland = parser.parse(CloudStorage.contentOfObject(s"summary/SCOTLAND/${date}.json"))
    val england = parser.parse(CloudStorage.contentOfObject(s"summary/ENGLAND/${date}.json"))
    val ireland = parser.parse(CloudStorage.contentOfObject(s"summary/NORTHERN IRELAND/${date}.json"))
    val channel = parser.parse(CloudStorage.contentOfObject(s"summary/CHANNEL ISLANDS/${date}.json"))

    val jsonObject = new JsonObject
    jsonObject.add("UK", uk)
    jsonObject.add("WALES", wales)
    jsonObject.add("SCOTLAND", scotland)
    jsonObject.add("ENGLAND", england)
    jsonObject.add("NORTHERN IRELAND", ireland)
    jsonObject.add("CHANNEL ISLANDS", channel)

    jsonObject
  }

  @RequestMapping(path = Array("/search"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def search(@RequestParam("name") name: String): JsonObject = {
    val  places = getPlaces("places")
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

  @RequestMapping(path = Array("c/places"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def counties(): JsonObject = {
    val  places = getPlaces("counties")
    val arr = new JsonArray
    val iter = places.iterator()
    while ( iter.hasNext ) { arr.add(iter.next())}
    val obj = new JsonObject
    obj.add("places", arr)
    obj
  }

  @RequestMapping(path = Array("/places"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def places(): JsonObject = {
    val  places = getPlaces("places")
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
    val date: String = s"${LocalDateTime.now().withNano(0).withHour(0).withMinute(0).withSecond(0)}"

    val places: String = CloudStorage.contentOfObject("register/places_register.json")
    val jobberz = parser.parse(places)

    if ( jobberz.getAsJsonObject.keySet().contains(name.trim)) {
      val route: String = jobberz.getAsJsonObject.get(name.trim).getAsString
      val contents: String = CloudStorage.contentOfObject(s"${route}${date}${name.trim}.json")
      val jobber = parser.parse(contents)
      jobber.getAsJsonObject
    }
    else{

      val places: String = CloudStorage.contentOfObject("register/counties_register.json")
      val jobberz = parser.parse(places)

      if ( jobberz.getAsJsonObject.keySet().contains(name.trim)) {
        val route: String = jobberz.getAsJsonObject.get(name.trim).getAsString
        val ls: String = CloudStorage.listObjects().filter(x => x.startsWith(s"${route}${date}")) .head
        val contents: String = CloudStorage.contentOfObject(ls)

        val jobber = parser.parse(contents)
        jobber.getAsJsonObject.addProperty("name", name.trim.capitalize)
        return jobber.getAsJsonObject
      }
      val defaultObj = new JsonObject
      defaultObj.addProperty("error", "PlaceNotFound")
      defaultObj
    }

  }

  @RequestMapping(path = Array("/n/place"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def tomorrowForecast(@RequestParam("name") name: String): JsonObject =  {
    val parser = new JsonParser
    val date: String = s"${LocalDateTime.now().withNano(0).withHour(0).withMinute(0).withSecond(0).plusDays(1)}"

    val places: String = CloudStorage.contentOfObject("register/places_register.json")
    val jobberz = parser.parse(places)

    if ( jobberz.getAsJsonObject.keySet().contains(name.trim)) {
      val route: String = jobberz.getAsJsonObject.get(name.trim).getAsString
      val contents: String = CloudStorage.contentOfObject(s"${route}${date}${name.trim}.json")
      val jobber = parser.parse(contents)
      jobber.getAsJsonObject
    }
    else{

      val places: String = CloudStorage.contentOfObject("register/counties_register.json")
      val jobberz = parser.parse(places)

      if ( jobberz.getAsJsonObject.keySet().contains(name.trim)) {
        val route: String = jobberz.getAsJsonObject.get(name.trim).getAsString
        val ls: String = CloudStorage.listObjects().filter(x => x.startsWith(s"${route}${date}")) .head
        val contents: String = CloudStorage.contentOfObject(ls)

        val jobber = parser.parse(contents)
        jobber.getAsJsonObject.addProperty("name", name.trim.capitalize)
        return jobber.getAsJsonObject
      }
      val defaultObj = new JsonObject
      defaultObj.addProperty("error", "PlaceNotFound")
      defaultObj
    }
  }

}
