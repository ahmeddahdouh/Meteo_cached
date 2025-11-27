package dz.ahmed.meteo_cahed.data.remote

import dz.ahmed.meteo_cahed.data.model.GeoCodingResponse
import dz.ahmed.meteo_cahed.data.model.WeatherForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("https://geocoding-api.open-meteo.com/v1/search")
    suspend fun searchCity(
        @Query("name") name: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "fr",
        @Query("format") format: String = "json"
    ): GeoCodingResponse

    @GET("v1/forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("models") models: String = "meteofrance_seamless",
        @Query("hourly") hourlyParams: String = "temperature_2m,relative_humidity_2m,apparent_temperature,rain,wind_speed_10m",
        @Query("current") currentParams: String = "temperature_2m,apparent_temperature,weather_code,wind_speed_10m",
        @Query("daily") dailyParams: String = "temperature_2m_max,temperature_2m_min",
        @Query("forecast_days") forecastDays: Int = 1,
        @Query("timezone") timezone: String = "auto"
    ): WeatherForecastResponse
}



