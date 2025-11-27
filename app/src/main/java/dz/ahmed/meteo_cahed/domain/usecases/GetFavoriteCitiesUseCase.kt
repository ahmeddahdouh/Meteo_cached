package dz.ahmed.meteo_cahed.domain.usecases

import dz.ahmed.meteo_cahed.data.repository.WeatherRepository

class GetFavoriteCitiesUseCase(
    private val repository: WeatherRepository
) {
    operator fun invoke() = repository.observeFavoriteSummaries()
}



