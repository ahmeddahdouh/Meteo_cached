package dz.ahmed.meteo_cahed.domain.usecases

import dz.ahmed.meteo_cahed.data.repository.WeatherRepository

class SearchCityUseCase(
    private val repository: WeatherRepository
) {
    operator fun invoke(query: String) = repository.searchCityFlow(query)
}



