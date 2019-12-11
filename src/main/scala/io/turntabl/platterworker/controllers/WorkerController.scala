package io.turntabl.platterworker.controllers

import org.springframework.web.bind.annotation.{GetMapping, RestController}

@RestController
class WorkerController {
  @GetMapping
  def listAll(): String =  "Hello There"
}
