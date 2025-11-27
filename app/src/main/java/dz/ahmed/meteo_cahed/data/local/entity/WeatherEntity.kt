package dz.ahmed.meteo_cahed.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherEntity(
    @PrimaryKey val cityId: Long,
    val cityName: String,
    val country: String?,
    val latitude: Double,
    val longitude: Double,
    val temperature: Double,
    val minTemperature: Double,
    val maxTemperature: Double,
    val windSpeed: Double,
    val conditionCode: Int,
    val conditionLabel: String,
    val iconName: String,
    val lastUpdated: Long,
    val isFavorite: Boolean
)

