package dz.ahmed.meteo_cahed.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dz.ahmed.meteo_cahed.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather_cache ORDER BY lastUpdated DESC")
    fun observeWeather(): Flow<List<WeatherEntity>>

    @Query("SELECT * FROM weather_cache WHERE cityId = :cityId LIMIT 1")
    suspend fun getByCityId(cityId: Long): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(weatherEntity: WeatherEntity)
}



