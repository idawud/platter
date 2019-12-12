package io.turntabl.platterworker.controllers

import java.nio.file.{Files, Paths}
import java.time.LocalDateTime

import com.google.gson.JsonParser
import io.turntabl.platterworker.AWS.CloudStorage
import io.turntabl.platterworker.services.WeatherDataProcessing
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RequestMethod, RestController}


@RestController
@RequestMapping(path = Array("/api/v1"))
class WorkerController {
  @GetMapping
  def index(): String = {
    "Welcome Home"
  }

  @RequestMapping(path = Array("/hello"), method = Array(RequestMethod.GET))
  def hello(): String =  {
    "Hello"
  }

  @RequestMapping(path = Array("/schedule"), method = Array(RequestMethod.GET))
  def scheduledTask(): String =  {
    val wp = new WeatherDataProcessing()
    val data = wp.toJsonString.toString

    val filename = s"${LocalDateTime.now().withNano(0)}.json"
    Files.createFile(Paths.get(filename))
    Files.write(Paths.get(filename), data.getBytes())
    CloudStorage.upload(Paths.get(filename))
    Files.delete(Paths.get(filename))
    data
  }
}