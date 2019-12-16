package io.turntabl.platterworker.controllers


import com.google.gson.{JsonObject, JsonParser}
import io.turntabl.platterworker.AWS.CloudStorage
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.{CrossOrigin, GetMapping, RequestMapping, RequestMethod, RestController}


@RestController
@CrossOrigin(origins = Array("*"))
@RequestMapping(path = Array("/api/v1"))
class WorkerController {
  @GetMapping
  def index(): String = "Welcome Home"

  @RequestMapping(path = Array("/hello"), method = Array(RequestMethod.GET))
  def hello(): String = "Hello"

  @RequestMapping(path = Array("/place"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def scheduledTask(): JsonObject =  {
    val parser = new JsonParser
    val content: String = CloudStorage.contentOfObject("WALES/Carmarthenshire/2019-12-13T16:10:42.json")
    val jobber = parser.parse(content)
    jobber.getAsJsonObject
  }
}
