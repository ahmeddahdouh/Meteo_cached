package dz.ahmed.meteo_cahed.ui.navigation

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dz.ahmed.meteo_cahed.MeteoApp
import dz.ahmed.meteo_cahed.domain.usecases.AddToFavoritesUseCase
import dz.ahmed.meteo_cahed.domain.usecases.GetFavoriteCitiesUseCase
import dz.ahmed.meteo_cahed.domain.usecases.GetWeatherForecastUseCase
import dz.ahmed.meteo_cahed.domain.usecases.RemoveFromFavoritesUseCase
import dz.ahmed.meteo_cahed.domain.usecases.SearchCityUseCase
import dz.ahmed.meteo_cahed.ui.details.DetailsViewModel
import dz.ahmed.meteo_cahed.ui.home.HomeViewModel
import dz.ahmed.meteo_cahed.ui.search.SearchViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val app = this.meteoApp()
            val repository = app.container.repository
            HomeViewModel(
                getFavoriteCitiesUseCase = GetFavoriteCitiesUseCase(repository),
                getWeatherForecastUseCase = GetWeatherForecastUseCase(repository),
                addToFavoritesUseCase = AddToFavoritesUseCase(repository),
                removeFromFavoritesUseCase = RemoveFromFavoritesUseCase(repository),
                locationClient = app.container.locationClient
            )
        }
        initializer {
            val savedStateHandle = createSavedStateHandle()
            val repository = this.meteoApp().container.repository
            SearchViewModel(
                searchCityUseCase = SearchCityUseCase(repository),
                addToFavoritesUseCase = AddToFavoritesUseCase(repository),
                removeFromFavoritesUseCase = RemoveFromFavoritesUseCase(repository),
                initialQuery = savedStateHandle.get<String>("initialQuery").orEmpty()
            )
        }
        initializer {
            val savedStateHandle = createSavedStateHandle()
            val repository = this.meteoApp().container.repository
            DetailsViewModel(
                savedStateHandle = savedStateHandle,
                getWeatherForecastUseCase = GetWeatherForecastUseCase(repository),
                addToFavoritesUseCase = AddToFavoritesUseCase(repository),
                removeFromFavoritesUseCase = RemoveFromFavoritesUseCase(repository),
                getFavoriteCitiesUseCase = GetFavoriteCitiesUseCase(repository)
            )
        }
    }
}

private fun CreationExtras.meteoApp(): MeteoApp =
    (this[androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MeteoApp)

