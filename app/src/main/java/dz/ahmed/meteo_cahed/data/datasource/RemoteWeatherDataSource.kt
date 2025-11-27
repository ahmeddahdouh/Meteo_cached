package dz.ahmed.meteo_cahed.data.datasource

import dz.ahmed.meteo_cahed.data.model.GeoCodingResult
import dz.ahmed.meteo_cahed.data.model.WeatherForecastResponse
import dz.ahmed.meteo_cahed.data.remote.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteWeatherDataSource(
    private val apiService: WeatherApiService
) {

    suspend fun searchCities(query: String): List<GeoCodingResult> = withContext(Dispatchers.IO) {
        apiService.searchCity(name = query).results.orEmpty()
    }

    suspend fun getWeatherForecast(latitude: Double, longitude: Double): WeatherForecastResponse =
        withContext(Dispatchers.IO) {
            apiService.getWeatherForecast(latitude = latitude, longitude = longitude)
        }
}



