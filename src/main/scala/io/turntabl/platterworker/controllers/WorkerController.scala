package io.turntabl.platterworker.controllers

import java.nio.file.{Files, Paths}
import java.time.LocalDateTime

import com.google.gson.JsonParser
import io.turntabl.platterworker.AWS.CloudStorage
import io.turntabl.platterworker.services.WeatherDataProcessing
import org.springframework.web.bind.annotation.{GetMapping, RestController}


@RestController
class WorkerController {

  @GetMapping(Array("/"))
  def index(): String = {
    "Welcome Home"
  }

  @GetMapping(path = Array("/schedule"))
  def listAll(): String =  {
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