package dz.ahmed.meteo_cahed.domain.usecases

import dz.ahmed.meteo_cahed.data.model.City
import dz.ahmed.meteo_cahed.data.repository.WeatherRepository

class AddToFavoritesUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: City) = repository.addToFavorites(city)
}



