package dz.ahmed.meteo_cahed.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

data class DetailsUiState(
    val city: City,
    val summary: WeatherSummary? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase
) : ViewModel() {

    private val cityArg = City(
        id = savedStateHandle.get<Long>("cityId") ?: 0L,
        name = savedStateHandle.get<String>("cityName") ?: "",
        country = savedStateHandle.get<String>("country")?.takeIf { it != "null" && it != "-" },
        latitude = savedStateHandle.get<String>("latitude")?.toDouble() ?: 0.0,
        longitude = savedStateHandle.get<String>("longitude")?.toDouble() ?: 0.0,
        timezone = null,
        isFavorite = false
    )

    private val _uiState = MutableStateFlow(DetailsUiState(city = cityArg))
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
        refreshWeather()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getFavoriteCitiesUseCase().collect { favorites ->
                val match = favorites.find { it.cityId == cityArg.id }
                _uiState.update { state ->
                    state.copy(
                        isFavorite = match != null,
                        summary = match ?: state.summary
                    )
                }
            }
        }
    }

    fun refreshWeather() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getWeatherForecastUseCase(_uiState.value.city.copy(isFavorite = _uiState.value.isFavorite))) {
                is ApiResult.Success -> _uiState.update {
                    it.copy(summary = result.data, isLoading = false)
                }
                is ApiResult.Error -> _uiState.update {
                    it.copy(
                        summary = result.cachedData ?: it.summary,
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                else -> Unit
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val current = _uiState.value
            if (current.isFavorite) {
                removeFromFavoritesUseCase(current.city.id)
            } else {
                addToFavoritesUseCase(current.city.copy(isFavorite = true))
            }
        }
    }
}

