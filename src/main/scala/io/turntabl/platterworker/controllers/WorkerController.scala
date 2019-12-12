package io.turntabl.platterworker.controllers

import java.nio.file.{Files, Paths}
import java.time.LocalDateTime

import io.turntabl.platterworker.AWS.CloudStorage
import io.turntabl.platterworker.services.WeatherDataProcessing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, RestController}

import scala.beans.BeanProperty


@RestController
class WorkerController {
  @GetMapping
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