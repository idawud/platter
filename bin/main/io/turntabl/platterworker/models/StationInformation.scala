package io.turntabl.platterworker.models

case class StationInformation(elevation: String, locationId: String, latitude: String, longitude: String, name: String, unitaryAuthArea: String)

case class StationForecast(information: StationInformation, country: String, dataDate: String, forecast: List[Forecast])

case class Forecast(timeStamp: String, data: WeatherData)

case class WeatherData(temperature: String, humidity: String, visibility: String, weatherType: String,
                       MaxUVIndex: String, feelsGoodTemp: String, windDirection: String, windGust: String,
                       precipitationProb: String, windSpeed: String)
