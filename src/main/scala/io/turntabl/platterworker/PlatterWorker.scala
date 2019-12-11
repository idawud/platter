package io.turntabl.platterworker

import java.time.LocalDateTime

import io.turntabl.platterworker.services.WeatherDataFetching
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

object PlatterWorker extends App {
 // SpringApplication.run(classOf[BootConfig])
  val we = new WeatherDataFetching
  println(we.forecastFromAllStations())
}

@SpringBootApplication
class BootConfig
