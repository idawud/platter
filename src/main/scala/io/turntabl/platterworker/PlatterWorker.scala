package io.turntabl.platterworker

import io.turntabl.platterworker.AWS.CloudStorage
import io.turntabl.platterworker.services.ServiceRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

object PlatterWorker extends App {
 SpringApplication.run(classOf[BootConfig])
 // ServiceRunner.run()

}

@SpringBootApplication
class BootConfig
