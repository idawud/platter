package io.turntabl.platterworker

import java.nio.file.{Files, Path, Paths}
import java.time.LocalDateTime

import com.google.gson.JsonObject
import io.turntabl.platterworker.AWS.CloudStorage
import io.turntabl.platterworker.services.{WeatherDataFetching, WeatherDataProcessing}
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

object PlatterWorker extends App {
 SpringApplication.run(classOf[BootConfig])
 // run()

 private def run(): Unit = {
  val wp = new WeatherDataProcessing()
  val fetch = new WeatherDataFetching
  val data: List[(String, JsonObject)] = fetch.forecastFromAllStations map wp.countyInfoToJsonString

  val timeStamp = s"${LocalDateTime.now().withNano(0)}"
  val path: Path = Files.createFile(Paths.get(s"${timeStamp}.json"))

  data foreach ( x => writeAndUpload(x, timeStamp, path))

  Files.delete(path)
 }

 private def writeAndUpload(data: (String, JsonObject), timeStamp: String, path: Path) = {
    Files.write(path, data._2.toString.getBytes())
    CloudStorage.upload(timeStamp, data._1, path)
  }

}

@SpringBootApplication
class BootConfig
