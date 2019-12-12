package io.turntabl.platterworker.controllers

import com.google.gson.JsonObject
import io.turntabl.platterworker.services.WeatherDataProcessing
import org.springframework.web.bind.annotation.{GetMapping, RestController}

@RestController
class WorkerController {
  @GetMapping
  def listAll(): String =  {
    val wp = new WeatherDataProcessing()
    wp.toJsonString().toString
  }
}
