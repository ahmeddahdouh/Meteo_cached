package dz.ahmed.meteo_cahed.data.repository

import dz.ahmed.meteo_cahed.data.model.ApiResult
import dz.ahmed.meteo_cahed.data.model.City
import dz.ahmed.meteo_cahed.data.model.WeatherSummary
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun searchCityFlow(query: String): Flow<ApiResult<List<City>>>
    fun observeFavoriteSummaries(): Flow<List<WeatherSummary>>
    fun isFavoriteFlow(cityId: Long): Flow<Boolean>
    suspend fun fetchWeatherForCity(city: City): ApiResult<WeatherSummary>
    suspend fun addToFavorites(city: City)
    suspend fun removeFromFavorites(cityId: Long)
    suspend fun refreshFavoritesWeather()
    suspend fun getFavoriteById(cityId: Long): City?
}



