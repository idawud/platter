package io.turntabl.platterworker

import io.turntabl.platterworker.services.WeatherDataProcessing
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

object PlatterWorker extends App {
 // SpringApplication.run(classOf[BootConfig])
 val wp = new WeatherDataProcessing()
 val gp = wp.toJsonString()
 println(gp)
}

@SpringBootApplication
class BootConfig
