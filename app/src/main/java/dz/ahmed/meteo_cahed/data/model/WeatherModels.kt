package dz.ahmed.meteo_cahed.data.model

data class City(
    val id: Long,
    val name: String,
    val country: String?,
    val latitude: Double,
    val longitude: Double,
    val timezone: String?,
    val isFavorite: Boolean = false
)

data class WeatherSummary(
    val cityId: Long,
    val cityName: String,
    val country: String?,
    val latitude: Double,
    val longitude: Double,
    val temperature: Double,
    val minTemperature: Double,
    val maxTemperature: Double,
    val windSpeed: Double,
    val condition: WeatherCondition,
    val lastUpdated: Long,
    val fromCache: Boolean,
    val isFavorite: Boolean
)

data class WeatherCondition(
    val code: Int,
    val label: String,
    val icon: WeatherIcon
)

enum class WeatherIcon {
    SUNNY, CLOUDY, RAINY, STORM, SNOW, FOG, WINDY, UNKNOWN
}

