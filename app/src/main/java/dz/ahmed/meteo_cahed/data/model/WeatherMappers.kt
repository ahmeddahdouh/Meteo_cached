package dz.ahmed.meteo_cahed.data.model

import kotlin.math.roundToInt

fun GeoCodingResult.toCity(isFavorite: Boolean = false): City {
    val safeId = id ?: "$name-$latitude-$longitude".hashCode().toLong()
    return City(
        id = safeId,
        name = name,
        country = listOfNotNull(country, admin1).joinToString(separator = " • ").ifBlank { null },
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        isFavorite = isFavorite
    )
}

fun WeatherForecastResponse.toWeatherSummary(
    city: City,
    isFavorite: Boolean,
    fromCache: Boolean = false,
    fallback: WeatherSummary? = null
): WeatherSummary {
    val temperature = current?.temperature ?: fallback?.temperature ?: hourly?.temperatures?.firstOrNull() ?: 0.0
    val windSpeed = current?.windSpeed ?: fallback?.windSpeed ?: hourly?.windSpeeds?.firstOrNull() ?: 0.0
    val minTemp = daily?.min?.firstOrNull() ?: fallback?.minTemperature ?: temperature
    val maxTemp = daily?.max?.firstOrNull() ?: fallback?.maxTemperature ?: temperature
    val code = current?.weatherCode ?: fallback?.condition?.code ?: 0
    val condition = WeatherCodeMapper.fromCode(code)

    return WeatherSummary(
        cityId = city.id,
        cityName = city.name,
        country = city.country,
        latitude = city.latitude,
        longitude = city.longitude,
        temperature = temperature,
        minTemperature = minTemp,
        maxTemperature = maxTemp,
        windSpeed = windSpeed,
        condition = condition,
        lastUpdated = System.currentTimeMillis(),
        fromCache = fromCache,
        isFavorite = isFavorite
    )
}

object WeatherCodeMapper {

    private val sunny = setOf(0)
    private val cloudy = setOf(1, 2, 3, 45, 48)
    private val rainy = setOf(51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82)
    private val storm = setOf(95, 96, 99)
    private val snow = setOf(71, 73, 75, 77, 85, 86)

    fun fromCode(code: Int): WeatherCondition {
        val (label, icon) = when (code) {
            in sunny -> "Ensoleillé" to WeatherIcon.SUNNY
            in cloudy -> "Nuageux" to WeatherIcon.CLOUDY
            in rainy -> "Pluvieux" to WeatherIcon.RAINY
            in storm -> "Orageux" to WeatherIcon.STORM
            in snow -> "Neige" to WeatherIcon.SNOW
            51, 53, 55 -> "Bruine" to WeatherIcon.RAINY
            61, 63 -> "Pluie modérée" to WeatherIcon.RAINY
            65 -> "Pluie forte" to WeatherIcon.RAINY
            80, 81, 82 -> "Averses" to WeatherIcon.RAINY
            else -> "Conditions inconnues" to WeatherIcon.UNKNOWN
        }
        return WeatherCondition(
            code = code,
            label = label,
            icon = icon
        )
    }
}

fun WeatherSummary.roundedTemperature(): String = "${temperature.roundToInt()}°"

