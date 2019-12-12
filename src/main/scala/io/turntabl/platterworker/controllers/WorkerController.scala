package io.turntabl.platterworker.controllers

import java.nio.file.{Files, Paths}
import java.time.LocalDateTime

import io.turntabl.platterworker.services.WeatherDataProcessing
import org.springframework.web.bind.annotation.{GetMapping, RestController}


@RestController
class WorkerController {
  @GetMapping
  def listAll(): String =  {
    val wp = new WeatherDataProcessing()
    val data = wp.toJsonString.toString

    val filename = s"${LocalDateTime.now().withNano(0)}.json"
    Files.createFile(Paths.get(filename))
    Files.write(Paths.get(filename), data.getBytes())

    data
  }
}
