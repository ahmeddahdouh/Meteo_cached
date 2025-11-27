package dz.ahmed.meteo_cahed.domain.usecases

import dz.ahmed.meteo_cahed.data.repository.WeatherRepository

class RemoveFromFavoritesUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityId: Long) = repository.removeFromFavorites(cityId)
}



