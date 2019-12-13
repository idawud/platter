package io.turntabl.platterworker

import java.nio.file.{Files, Paths}
import java.time.LocalDateTime

import io.turntabl.platterworker.AWS.CloudStorage
import io.turntabl.platterworker.services.WeatherDataProcessing
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

object PlatterWorker extends App {
 // SpringApplication.run(classOf[BootConfig])
// val wp = new WeatherDataProcessing()
// val data = wp.toJsonString.toString
//
// val filename = s"${LocalDateTime.now().withNano(0)}.json"
// Files.createFile(Paths.get(filename))
// Files.write(Paths.get(filename), data.getBytes())
// CloudStorage.upload(Paths.get(filename))
// Files.delete(Paths.get(filename))
 println(sys.env.getOrElse("aws_acces", "aws_x"))
}

@SpringBootApplication
class BootConfig
