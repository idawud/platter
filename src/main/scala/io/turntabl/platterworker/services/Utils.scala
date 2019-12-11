package io.turntabl.platterworker.services

import java.net.URI
import java.net.http.{HttpClient, HttpRequest, HttpResponse}

object Utils {
    val API_BASE: String = "http://datapoint.metoffice.gov.uk/public/data/val"
    val API_KEY: String = "?res=3hourly&key=ca3ee938-3ade-405e-adc1-38990741404b"

    def fetchDataFromApiEndpoint( url: String): String = {
      val request = HttpRequest.newBuilder().uri(new URI(API_BASE + url + API_KEY)).build()
      val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString)
      response.body()
    }
}
