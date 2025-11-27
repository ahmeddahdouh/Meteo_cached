package dz.ahmed.meteo_cahed.data.repository

import dz.ahmed.meteo_cahed.data.datasource.LocalWeatherDataSource
import dz.ahmed.meteo_cahed.data.datasource.RemoteWeatherDataSource
import dz.ahmed.meteo_cahed.data.datasource.UserPreferencesDataSource
import dz.ahmed.meteo_cahed.data.local.toDomain
import dz.ahmed.meteo_cahed.data.local.toEntity
import dz.ahmed.meteo_cahed.data.local.toDomain as weatherToDomain
import dz.ahmed.meteo_cahed.data.local.toEntity as weatherToEntity
import dz.ahmed.meteo_cahed.data.model.ApiResult
import dz.ahmed.meteo_cahed.data.model.City
import dz.ahmed.meteo_cahed.data.model.WeatherCodeMapper
import dz.ahmed.meteo_cahed.data.model.WeatherSummary
import dz.ahmed.meteo_cahed.data.model.toWeatherSummary
import dz.ahmed.meteo_cahed.data.model.toCity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WeatherRepositoryImpl(
    private val remoteDataSource: RemoteWeatherDataSource,
    private val localDataSource: LocalWeatherDataSource,
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : WeatherRepository {

    override fun searchCityFlow(query: String): Flow<ApiResult<List<City>>> = flow {
        if (query.length < 2) {
            emit(ApiResult.Success(emptyList()))
            return@flow
        }
        emit(ApiResult.Loading())
        val favoriteIds = userPreferencesDataSource.favoriteIds.first()
        runCatching {
            remoteDataSource.searchCities(query)
                .map { result ->
                    val city = result.toCity()
                    if (favoriteIds.contains(city.id)) city.copy(isFavorite = true) else city
                }
        }.onSuccess { cities ->
            if (cities.isEmpty()) {
                emit(ApiResult.Error(message = "Aucune ville trouvée."))
            } else {
                emit(ApiResult.Success(cities))
            }
        }.onFailure { throwable ->
            val cache = localDataSource.observeFavoriteCities().first().map { it.toDomain() }
            emit(ApiResult.Error(message = throwable.message ?: "Erreur réseau", cause = throwable, cachedData = cache))
        }
    }

    override fun observeFavoriteSummaries(): Flow<List<WeatherSummary>> =
        combine(
            localDataSource.observeFavoriteCities(),
            localDataSource.observeWeatherCache()
        ) { favoriteEntities, weatherEntities ->
            val weatherMap = weatherEntities.associateBy { it.cityId }
            val defaultCondition = WeatherCodeMapper.fromCode(0)
            favoriteEntities.map { favorite ->
                weatherMap[favorite.cityId]?.weatherToDomain()
                    ?: WeatherSummary(
                        cityId = favorite.cityId,
                        cityName = favorite.name,
                        country = favorite.country,
                        latitude = favorite.latitude,
                        longitude = favorite.longitude,
                        temperature = 0.0,
                        minTemperature = 0.0,
                        maxTemperature = 0.0,
                        windSpeed = 0.0,
                        condition = defaultCondition,
                        lastUpdated = 0,
                        fromCache = true,
                        isFavorite = true
                    )
            }
        }

    override fun isFavoriteFlow(cityId: Long): Flow<Boolean> =
        userPreferencesDataSource.favoriteIds.map { ids -> ids.contains(cityId) }

    override suspend fun fetchWeatherForCity(city: City): ApiResult<WeatherSummary> =
        withContext(ioDispatcher) {
            val favorites = userPreferencesDataSource.favoriteIds.first()
            val isFavorite = favorites.contains(city.id)
            runCatching {
                remoteDataSource.getWeatherForecast(city.latitude, city.longitude)
            }.fold(
                onSuccess = { response ->
                    val summary = response.toWeatherSummary(city, isFavorite = isFavorite)
                    localDataSource.saveWeather(summary.weatherToEntity())
                    ApiResult.Success(summary)
                },
                onFailure = { throwable ->
                    val cached = localDataSource.getWeather(city.id)?.weatherToDomain()
                    ApiResult.Error(
                        message = throwable.message ?: "Erreur lors de la récupération météo",
                        cause = throwable,
                        cachedData = cached
                    )
                }
            )
        }

    override suspend fun addToFavorites(city: City) = withContext(ioDispatcher) {
        localDataSource.saveFavorite(city.toEntity())
        userPreferencesDataSource.addFavorite(city.id)
    }

    override suspend fun removeFromFavorites(cityId: Long) = withContext(ioDispatcher) {
        localDataSource.removeFavorite(cityId)
        userPreferencesDataSource.removeFavorite(cityId)
    }

    override suspend fun refreshFavoritesWeather() = withContext(ioDispatcher) {
        val favorites = localDataSource.observeFavoriteCities().first()
        favorites.forEach { favorite ->
            runCatching {
                val city = favorite.toDomain()
                remoteDataSource.getWeatherForecast(city.latitude, city.longitude)
                    .toWeatherSummary(city = city, isFavorite = true)
            }.onSuccess { summary ->
                localDataSource.saveWeather(summary.weatherToEntity())
            }
        }
    }

    override suspend fun getFavoriteById(cityId: Long): City? = withContext(ioDispatcher) {
        localDataSource.getFavorite(cityId)?.toDomain()
    }
}

