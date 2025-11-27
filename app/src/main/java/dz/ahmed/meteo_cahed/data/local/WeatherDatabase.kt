package dz.ahmed.meteo_cahed.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dz.ahmed.meteo_cahed.data.local.dao.FavoriteCityDao
import dz.ahmed.meteo_cahed.data.local.dao.WeatherDao
import dz.ahmed.meteo_cahed.data.local.entity.FavoriteCityEntity
import dz.ahmed.meteo_cahed.data.local.entity.WeatherEntity

@Database(
    entities = [
        FavoriteCityEntity::class,
        WeatherEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun favoriteCityDao(): FavoriteCityDao
    abstract fun weatherDao(): WeatherDao
}

