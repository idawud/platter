package io.turntabl.platterworker.controllers


import com.google.gson.{JsonObject, JsonParser}
import io.turntabl.platterworker.AWS.CloudStorage
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation._


@RestController
@CrossOrigin(origins = Array("*"))
@RequestMapping(path = Array("/api/v1"))
class WorkerController() {

  @GetMapping
  def index(): String = {
    "done"
  }

  @RequestMapping(path = Array("/he"), method = Array(RequestMethod.GET))
  def he(): String = "service.nu()"

  @RequestMapping(path = Array("/hello"), method = Array(RequestMethod.GET))
  def hello(): String = "Hello"

  @RequestMapping(path = Array("/place"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def scheduledTask(@RequestParam("name") name: String): JsonObject =  {
    val parser = new JsonParser
    val content: String = CloudStorage.contentOfObject(s"${name}/2019-12-17T00:00.json")
    val jobber = parser.parse(content)
    jobber.getAsJsonObject
  }
}
