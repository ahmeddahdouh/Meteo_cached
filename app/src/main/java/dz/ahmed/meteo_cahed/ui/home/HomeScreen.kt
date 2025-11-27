package dz.ahmed.meteo_cahed.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dz.ahmed.meteo_cahed.R
import dz.ahmed.meteo_cahed.data.model.WeatherSummary
import dz.ahmed.meteo_cahed.ui.components.CitySearchBar
import dz.ahmed.meteo_cahed.ui.components.DayNightHeader
import dz.ahmed.meteo_cahed.ui.components.StateMessageCard
import dz.ahmed.meteo_cahed.ui.components.WeatherCard
import dz.ahmed.meteo_cahed.ui.navigation.AppViewModelProvider

@Composable
fun HomeRoute(
    onSearchRequested: (String) -> Unit,
    onWeatherSelected: (WeatherSummary) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState,
        onSearchRequested = onSearchRequested,
        onWeatherSelected = onWeatherSelected,
        onRefresh = viewModel::refreshFavorites,
        onRemoveFavorite = viewModel::removeFromFavorites,
        onUseCurrentLocation = viewModel::onUseCurrentLocation,
        onAddCurrentLocationToFavorites = viewModel::addCurrentLocationToFavorites,
        onDismissMessage = viewModel::clearMessage,
        modifier = modifier
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onSearchRequested: (String) -> Unit,
    onWeatherSelected: (WeatherSummary) -> Unit,
    onRefresh: () -> Unit,
    onRemoveFavorite: (Long) -> Unit,
    onUseCurrentLocation: () -> Unit,
    onAddCurrentLocationToFavorites: () -> Unit,
    onDismissMessage: () -> Unit,
    modifier: Modifier = Modifier
) {
    var query by rememberSaveable { mutableStateOf("") }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onRefresh,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = "Rafraîchir"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            item {
                DayNightHeader(
                    title = stringResource(id = R.string.home_title)
                )
                Spacer(modifier = Modifier.height(8.dp))
                CitySearchBar(
                    value = query,
                    onValueChange = { query = it },
                    onSearch = { onSearchRequested(query) },
                    placeholder = stringResource(id = R.string.home_search_placeholder)
                )
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = onUseCurrentLocation,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.home_use_location),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            uiState.locationWeather?.let { summary ->
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = stringResource(id = R.string.home_nearby_weather),
                            style = MaterialTheme.typography.titleMedium
                        )
                        WeatherCard(
                            summary = summary,
                            onClick = { onWeatherSelected(summary) },
                            onFavoriteToggle = onAddCurrentLocationToFavorites
                        )
                    }
                }
            }
            if (uiState.isOffline) {
                item {
                    StateMessageCard(
                        title = stringResource(id = R.string.home_offline_title),
                        message = stringResource(id = R.string.home_offline_message),
                        actionLabel = stringResource(id = R.string.home_retry),
                        onAction = onRefresh
                    )
                }
            }
            uiState.errorMessage?.let { message ->
                item {
                    StateMessageCard(
                        title = stringResource(id = R.string.home_error_title),
                        message = message,
                        actionLabel = stringResource(id = R.string.home_retry),
                        onAction = {
                            onDismissMessage()
                            onRefresh()
                        }
                    )
                }
            }
            if (uiState.favorites.isEmpty()) {
                item {
                    StateMessageCard(
                        title = stringResource(id = R.string.home_empty_title),
                        message = stringResource(id = R.string.home_empty_message)
                    )
                }
            } else {
                item {
                    Text(
                        text = stringResource(id = R.string.home_favorites_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                items(
                    items = uiState.favorites,
                    key = { it.cityId }
                ) { summary ->
                    WeatherCard(
                        summary = summary,
                        onClick = { onWeatherSelected(summary) },
                        onFavoriteToggle = { onRemoveFavorite(summary.cityId) }
                    )
                }
            }
        }
    }
}

