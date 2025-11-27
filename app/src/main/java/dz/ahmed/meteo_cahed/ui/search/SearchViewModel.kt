package dz.ahmed.meteo_cahed.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dz.ahmed.meteo_cahed.data.model.ApiResult
import dz.ahmed.meteo_cahed.data.model.City
import dz.ahmed.meteo_cahed.domain.usecases.AddToFavoritesUseCase
import dz.ahmed.meteo_cahed.domain.usecases.RemoveFromFavoritesUseCase
import dz.ahmed.meteo_cahed.domain.usecases.SearchCityUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val results: List<City> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class SearchViewModel(
    private val searchCityUseCase: SearchCityUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    initialQuery: String = ""
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState(query = initialQuery))
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        if (initialQuery.length >= 2) {
            search(initialQuery)
        }
    }

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value, errorMessage = null) }
    }

    fun search(query: String = _uiState.value.query) {
        if (query.length < 2) {
            _uiState.update { it.copy(results = emptyList(), errorMessage = null) }
            return
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchCityUseCase(query).collect { result ->
                when (result) {
                    is ApiResult.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is ApiResult.Success -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            results = result.data,
                            errorMessage = null
                        )
                    }
                    is ApiResult.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            results = result.cachedData.orEmpty(),
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }

    fun toggleFavorite(city: City) {
        viewModelScope.launch {
            if (city.isFavorite) {
                removeFromFavoritesUseCase(city.id)
            } else {
                addToFavoritesUseCase(city.copy(isFavorite = true))
            }
            _uiState.update { state ->
                state.copy(
                    results = state.results.map {
                        if (it.id == city.id) it.copy(isFavorite = !city.isFavorite) else it
                    }
                )
            }
        }
    }
}

