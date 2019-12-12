package io.turntabl.platterworker.controllers

import java.nio.file.{Files, Paths}
import java.time.LocalDateTime

import com.google.gson.JsonObject
import io.turntabl.platterworker.AWS.CloudStorage
import io.turntabl.platterworker.services.WeatherDataProcessing
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.{GetMapping, RestController}


@RestController
class WorkerController {

  @GetMapping(Array("/"))
  def index(): String = {
    "Welcome Home"
  }

  @GetMapping(path = Array("/schedule"),  produces=Array(MediaType.APPLICATION_JSON_VALUE))
  def listAll(): JsonObject =  {
    val wp = new WeatherDataProcessing()
    val de = wp.toJsonString
    val data = de.toString

    val filename = s"${LocalDateTime.now().withNano(0)}.json"
    Files.createFile(Paths.get(filename))
    Files.write(Paths.get(filename), data.getBytes())
    CloudStorage.upload(Paths.get(filename))
    Files.delete(Paths.get(filename))
    de
  }
}