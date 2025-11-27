package dz.ahmed.meteo_cahed.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoCodingResponse(
    val results: List<GeoCodingResult>? = emptyList()
)

@Serializable
data class GeoCodingResult(
    val id: Long? = null,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String? = null,
    @SerialName("admin1") val admin1: String? = null,
    val timezone: String? = null,
    val population: Int? = null
)

@Serializable
data class WeatherForecastResponse(
    val latitude: Double,
    val longitude: Double,
    val timezone: String? = null,
    @SerialName("current_units") val currentUnits: CurrentUnits? = null,
    val current: Current? = null,
    @SerialName("hourly_units") val hourlyUnits: HourlyUnits? = null,
    val hourly: Hourly? = null,
    @SerialName("daily_units") val dailyUnits: DailyUnits? = null,
    val daily: Daily? = null
)

@Serializable
data class CurrentUnits(
    @SerialName("temperature_2m") val temperature: String? = null,
    @SerialName("wind_speed_10m") val windSpeed: String? = null
)

@Serializable
data class Current(
    @SerialName("time") val time: String? = null,
    @SerialName("temperature_2m") val temperature: Double? = null,
    @SerialName("apparent_temperature") val apparentTemperature: Double? = null,
    @SerialName("wind_speed_10m") val windSpeed: Double? = null,
    @SerialName("weather_code") val weatherCode: Int? = null
)

@Serializable
data class HourlyUnits(
    @SerialName("temperature_2m") val temperature: String? = null,
    @SerialName("wind_speed_10m") val windSpeed: String? = null,
    val rain: String? = null
)

@Serializable
data class Hourly(
    val time: List<String> = emptyList(),
    @SerialName("temperature_2m") val temperatures: List<Double> = emptyList(),
    val rain: List<Double> = emptyList(),
    @SerialName("wind_speed_10m") val windSpeeds: List<Double> = emptyList()
)

@Serializable
data class DailyUnits(
    @SerialName("temperature_2m_max") val max: String? = null,
    @SerialName("temperature_2m_min") val min: String? = null
)

@Serializable
data class Daily(
    @SerialName("time") val time: List<String> = emptyList(),
    @SerialName("temperature_2m_max") val max: List<Double> = emptyList(),
    @SerialName("temperature_2m_min") val min: List<Double> = emptyList()
)



