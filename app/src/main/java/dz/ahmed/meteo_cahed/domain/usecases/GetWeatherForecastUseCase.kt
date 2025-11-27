package dz.ahmed.meteo_cahed.domain.usecases

import dz.ahmed.meteo_cahed.data.model.ApiResult
import dz.ahmed.meteo_cahed.data.model.City
import dz.ahmed.meteo_cahed.data.model.WeatherSummary
import dz.ahmed.meteo_cahed.data.repository.WeatherRepository

class GetWeatherForecastUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: City): ApiResult<WeatherSummary> =
        repository.fetchWeatherForCity(city)
}



