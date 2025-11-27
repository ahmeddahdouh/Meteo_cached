package dz.ahmed.meteo_cahed.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dz.ahmed.meteo_cahed.data.local.entity.FavoriteCityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCityDao {

    @Query("SELECT * FROM favorite_cities ORDER BY name ASC")
    fun observeFavorites(): Flow<List<FavoriteCityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(favoriteCityEntity: FavoriteCityEntity)

    @Query("DELETE FROM favorite_cities WHERE cityId = :cityId")
    suspend fun delete(cityId: Long)

    @Query("SELECT * FROM favorite_cities WHERE cityId = :cityId LIMIT 1")
    suspend fun getById(cityId: Long): FavoriteCityEntity?
}



