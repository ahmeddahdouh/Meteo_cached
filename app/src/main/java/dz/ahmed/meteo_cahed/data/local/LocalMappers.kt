package dz.ahmed.meteo_cahed.data.local

import dz.ahmed.meteo_cahed.data.local.entity.FavoriteCityEntity
import dz.ahmed.meteo_cahed.data.local.entity.WeatherEntity
import dz.ahmed.meteo_cahed.data.model.City
import dz.ahmed.meteo_cahed.data.model.WeatherCondition
import dz.ahmed.meteo_cahed.data.model.WeatherIcon
import dz.ahmed.meteo_cahed.data.model.WeatherSummary

fun FavoriteCityEntity.toDomain(isFavorite: Boolean = true) = City(
    id = cityId,
    name = name,
    country = country,
    latitude = latitude,
    longitude = longitude,
    timezone = timezone,
    isFavorite = isFavorite
)

fun City.toEntity() = FavoriteCityEntity(
    cityId = id,
    name = name,
    country = country,
    latitude = latitude,
    longitude = longitude,
    timezone = timezone
)

fun WeatherEntity.toDomain() = WeatherSummary(
    cityId = cityId,
    cityName = cityName,
    country = country,
    latitude = latitude,
    longitude = longitude,
    temperature = temperature,
    minTemperature = minTemperature,
    maxTemperature = maxTemperature,
    windSpeed = windSpeed,
    condition = WeatherCondition(
        code = conditionCode,
        label = conditionLabel,
        icon = WeatherIcon.valueOf(iconName)
    ),
    lastUpdated = lastUpdated,
    fromCache = true,
    isFavorite = isFavorite
)

fun WeatherSummary.toEntity() = WeatherEntity(
    cityId = cityId,
    cityName = cityName,
    country = country,
    latitude = latitude,
    longitude = longitude,
    temperature = temperature,
    minTemperature = minTemperature,
    maxTemperature = maxTemperature,
    windSpeed = windSpeed,
    conditionCode = condition.code,
    conditionLabel = condition.label,
    iconName = condition.icon.name,
    lastUpdated = lastUpdated,
    isFavorite = isFavorite
)

