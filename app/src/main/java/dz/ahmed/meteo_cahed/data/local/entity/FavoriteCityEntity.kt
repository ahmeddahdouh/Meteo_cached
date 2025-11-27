package dz.ahmed.meteo_cahed.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cities")
data class FavoriteCityEntity(
    @PrimaryKey val cityId: Long,
    val name: String,
    val country: String?,
    val latitude: Double,
    val longitude: Double,
    val timezone: String?,
    val createdAt: Long = System.currentTimeMillis()
)



