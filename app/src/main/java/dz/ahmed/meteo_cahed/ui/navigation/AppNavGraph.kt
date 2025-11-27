package dz.ahmed.meteo_cahed.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dz.ahmed.meteo_cahed.data.model.WeatherSummary
import dz.ahmed.meteo_cahed.ui.details.DetailsRoute
import dz.ahmed.meteo_cahed.ui.home.HomeRoute
import dz.ahmed.meteo_cahed.ui.search.SearchRoute

object WeatherDestinations {
    const val HOME = "home"
    const val SEARCH_ROUTE = "search?initialQuery={initialQuery}"
    const val DETAILS_ROUTE = "details/{cityId}/{cityName}/{country}/{latitude}/{longitude}"

    fun searchRoute(query: String) = "search?initialQuery=${Uri.encode(query)}"

    fun detailsRoute(summary: WeatherSummary): String = detailsRoute(
        cityId = summary.cityId,
        name = summary.cityName,
        country = summary.country,
        latitude = summary.latitude,
        longitude = summary.longitude
    )

    fun detailsRoute(
        cityId: Long,
        name: String,
        country: String?,
        latitude: Double,
        longitude: Double
    ): String {
        val encodedName = Uri.encode(name)
        val encodedCountry = Uri.encode(country ?: "-")
        return "details/$cityId/$encodedName/$encodedCountry/$latitude/$longitude"
    }
}

@Composable
fun MeteoNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = WeatherDestinations.HOME
    ) {
        composable(WeatherDestinations.HOME) {
            HomeRoute(
                onSearchRequested = { query ->
                    navController.navigate(WeatherDestinations.searchRoute(query))
                },
                onWeatherSelected = { summary ->
                    navController.navigate(WeatherDestinations.detailsRoute(summary))
                }
            )
        }
        composable(
            route = WeatherDestinations.SEARCH_ROUTE,
            arguments = listOf(
                navArgument("initialQuery") {
                    nullable = true
                    defaultValue = ""
                }
            )
        ) {
            SearchRoute(
                onBack = { navController.popBackStack() },
                onCitySelected = { city ->
                    navController.navigate(
                        WeatherDestinations.detailsRoute(
                            cityId = city.id,
                            name = city.name,
                            country = city.country,
                            latitude = city.latitude,
                            longitude = city.longitude
                        )
                    )
                }
            )
        }
        composable(
            route = WeatherDestinations.DETAILS_ROUTE,
            arguments = listOf(
                navArgument("cityId") { type = NavType.LongType },
                navArgument("cityName") { type = NavType.StringType },
                navArgument("country") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = "-"
                },
                navArgument("latitude") { type = NavType.StringType },
                navArgument("longitude") { type = NavType.StringType }
            )
        ) {
            DetailsRoute(onBack = { navController.popBackStack() })
        }
    }
}

