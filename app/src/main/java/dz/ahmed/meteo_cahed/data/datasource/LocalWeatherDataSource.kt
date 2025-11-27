package dz.ahmed.meteo_cahed.data.datasource

import dz.ahmed.meteo_cahed.data.local.dao.FavoriteCityDao
import dz.ahmed.meteo_cahed.data.local.dao.WeatherDao
import dz.ahmed.meteo_cahed.data.local.entity.FavoriteCityEntity
import dz.ahmed.meteo_cahed.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

class LocalWeatherDataSource(
    private val favoriteCityDao: FavoriteCityDao,
    private val weatherDao: WeatherDao
) {

    fun observeFavoriteCities(): Flow<List<FavoriteCityEntity>> = favoriteCityDao.observeFavorites()

    fun observeWeatherCache(): Flow<List<WeatherEntity>> = weatherDao.observeWeather()

    suspend fun saveFavorite(city: FavoriteCityEntity) = favoriteCityDao.upsert(city)

    suspend fun removeFavorite(cityId: Long) = favoriteCityDao.delete(cityId)

    suspend fun getFavorite(cityId: Long): FavoriteCityEntity? = favoriteCityDao.getById(cityId)

    suspend fun saveWeather(weatherEntity: WeatherEntity) = weatherDao.upsert(weatherEntity)

    suspend fun getWeather(cityId: Long): WeatherEntity? = weatherDao.getByCityId(cityId)
}



