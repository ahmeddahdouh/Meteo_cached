package dz.ahmed.meteo_cahed.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dz.ahmed.meteo_cahed.data.location.LocationClient
import dz.ahmed.meteo_cahed.data.location.LocationResult
import dz.ahmed.meteo_cahed.data.model.ApiResult
import dz.ahmed.meteo_cahed.data.model.City
import dz.ahmed.meteo_cahed.data.model.WeatherSummary
import dz.ahmed.meteo_cahed.domain.usecases.AddToFavoritesUseCase
import dz.ahmed.meteo_cahed.domain.usecases.GetFavoriteCitiesUseCase
import dz.ahmed.meteo_cahed.domain.usecases.GetWeatherForecastUseCase
import dz.ahmed.meteo_cahed.domain.usecases.RemoveFromFavoritesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val favorites: List<WeatherSummary> = emptyList(),
    val locationWeather: WeatherSummary? = null,
    val isLoading: Boolean = false,
    val isOffline: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(
    private val getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase,
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val locationClient: LocationClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getFavoriteCitiesUseCase().collect { summaries ->
                _uiState.update { state ->
                    state.copy(
                        favorites = summaries,
                        isLoading = false,
                        isOffline = false,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun refreshFavorites() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val favorites = _uiState.value.favorites
            favorites.forEach { summary ->
                val city = City(
                    id = summary.cityId,
                    name = summary.cityName,
                    country = summary.country,
                    latitude = summary.latitude,
                    longitude = summary.longitude,
                    timezone = null,
                    isFavorite = true
                )
                when (val result = getWeatherForecastUseCase(city)) {
                    is ApiResult.Success -> _uiState.update { state ->
                        val updatedFavorites = state.favorites.map { fav ->
                            if (fav.cityId == city.id) result.data else fav
                        }
                        state.copy(favorites = updatedFavorites, isLoading = false)
                    }
                    is ApiResult.Error -> _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            isOffline = true,
                            errorMessage = result.message
                        )
                    }
                    else -> Unit
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun removeFromFavorites(cityId: Long) {
        viewModelScope.launch {
            removeFromFavoritesUseCase(cityId)
        }
    }

    fun onUseCurrentLocation() {
        viewModelScope.launch {
            when (val location = locationClient.getCurrentLocation()) {
                is LocationResult.Success -> {
                    val existingFavorite = _uiState.value.favorites.any {
                        it.latitude == location.latitude && it.longitude == location.longitude
                    }
                    val city = City(
                        id = "${location.latitude}${location.longitude}".hashCode().toLong(),
                        name = "Ma position",
                        country = null,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        timezone = null,
                        isFavorite = existingFavorite
                    )
                    when (val result = getWeatherForecastUseCase(city)) {
                        is ApiResult.Success -> _uiState.update { state ->
                            state.copy(
                                locationWeather = result.data,
                                errorMessage = null
                            )
                        }
                        is ApiResult.Error -> _uiState.update { it.copy(errorMessage = result.message) }
                        else -> Unit
                    }
                }
                is LocationResult.Error -> _uiState.update { it.copy(errorMessage = location.throwable.message) }
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun addCurrentLocationToFavorites() {
        val weather = _uiState.value.locationWeather ?: return
        viewModelScope.launch {
            val city = City(
                id = weather.cityId,
                name = weather.cityName,
                country = weather.country,
                latitude = weather.latitude,
                longitude = weather.longitude,
                timezone = null,
                isFavorite = true
            )
            addToFavoritesUseCase(city)
            _uiState.update { it.copy(locationWeather = weather.copy(isFavorite = true)) }
        }
    }
}

