package io.turntabl.platterworker.controllers


import com.google.gson.{JsonObject, JsonParser}
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RequestMethod, RestController}


@RestController
@RequestMapping(path = Array("/api/v1"))
class WorkerController {
  @GetMapping
  def index(): String = "Welcome Home"

  @RequestMapping(path = Array("/hello"), method = Array(RequestMethod.GET))
  def hello(): String = "Hello"

  @RequestMapping(path = Array("/schedule"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def scheduledTask(): JsonObject =  {
    val parser = new JsonParser
    val jobber = parser.parse("{\"country\":\"ENGLAND\", \"county\":\"Carmarthenshire\", \"elevation\":\"4.0\",\"locationId\":\"3872\",\"latitude\":\"50.815\",\"longitude\":\"-0.923\",\"name\":\"Thorney Island\",\"periods\":[{\"timestamp\":\"2019-12-14T00:00:00Z\",\"reading\":{\"temperature\":\"7\",\"feelsGoodTemp\":\"3\",\"humidity\":\"90\",\"MaxUVIndex\":\"0\",\"precipitationProb\":\"87\",\"visibility\":\"VG\",\"weatherType\":\"15\",\"windDirection\":\"WNW\",\"windGust\":\"18\",\"windSpeed\":\"9\"}},{\"timestamp\":\"2019-12-14T03:00:00Z\",\"reading\":{\"temperature\":\"6\",\"feelsGoodTemp\":\"1\",\"humidity\":\"81\",\"MaxUVIndex\":\"0\",\"precipitationProb\":\"36\",\"visibility\":\"VG\",\"weatherType\":\"2\",\"windDirection\":\"W\",\"windGust\":\"38\",\"windSpeed\":\"22\"}},{\"timestamp\":\"2019-12-14T06:00:00Z\",\"reading\":{\"temperature\":\"6\",\"feelsGoodTemp\":\"1\",\"humidity\":\"73\",\"MaxUVIndex\":\"0\",\"precipitationProb\":\"17\",\"visibility\":\"EX\",\"weatherType\":\"7\",\"windDirection\":\"W\",\"windGust\":\"31\",\"windSpeed\":\"18\"}},{\"timestamp\":\"2019-12-14T09:00:00Z\",\"reading\":{\"temperature\":\"7\",\"feelsGoodTemp\":\"2\",\"humidity\":\"76\",\"MaxUVIndex\":\"1\",\"precipitationProb\":\"37\",\"visibility\":\"EX\",\"weatherType\":\"10\",\"windDirection\":\"WSW\",\"windGust\":\"40\",\"windSpeed\":\"27\"}},{\"timestamp\":\"2019-12-14T12:00:00Z\",\"reading\":{\"temperature\":\"7\",\"feelsGoodTemp\":\"2\",\"humidity\":\"68\",\"MaxUVIndex\":\"1\",\"precipitationProb\":\"4\",\"visibility\":\"EX\",\"weatherType\":\"3\",\"windDirection\":\"W\",\"windGust\":\"40\",\"windSpeed\":\"25\"}},{\"timestamp\":\"2019-12-14T15:00:00Z\",\"reading\":{\"temperature\":\"7\",\"feelsGoodTemp\":\"3\",\"humidity\":\"61\",\"MaxUVIndex\":\"1\",\"precipitationProb\":\"0\",\"visibility\":\"EX\",\"weatherType\":\"1\",\"windDirection\":\"W\",\"windGust\":\"34\",\"windSpeed\":\"20\"}},{\"timestamp\":\"2019-12-14T18:00:00Z\",\"reading\":{\"temperature\":\"7\",\"feelsGoodTemp\":\"3\",\"humidity\":\"74\",\"MaxUVIndex\":\"0\",\"precipitationProb\":\"7\",\"visibility\":\"EX\",\"weatherType\":\"2\",\"windDirection\":\"SW\",\"windGust\":\"22\",\"windSpeed\":\"11\"}},{\"timestamp\":\"2019-12-14T21:00:00Z\",\"reading\":{\"temperature\":\"9\",\"feelsGoodTemp\":\"5\",\"humidity\":\"81\",\"MaxUVIndex\":\"0\",\"precipitationProb\":\"44\",\"visibility\":\"VG\",\"weatherType\":\"9\",\"windDirection\":\"SSW\",\"windGust\":\"43\",\"windSpeed\":\"25\"}}],\"dataDate\":\"2019-12-13T15:00:00Z\"}")

    jobber.getAsJsonObject
  }
}