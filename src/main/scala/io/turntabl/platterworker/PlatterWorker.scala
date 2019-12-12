package io.turntabl.platterworker

import io.turntabl.platterworker.services.WeatherDataProcessing
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

object PlatterWorker extends App {
 SpringApplication.run(classOf[BootConfig])
}

@SpringBootApplication
class BootConfig
