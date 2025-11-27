package dz.ahmed.meteo_cahed.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesDataSource(
    private val dataStore: DataStore<Preferences>
) {

    private val favoritesKey = stringSetPreferencesKey("favorite_ids")

    val favoriteIds: Flow<Set<Long>> = dataStore.data.map { preferences ->
        preferences[favoritesKey].orEmpty().mapNotNull { it.toLongOrNull() }.toSet()
    }

    suspend fun addFavorite(id: Long) {
        dataStore.edit { prefs ->
            val current = prefs[favoritesKey].orEmpty().toMutableSet()
            current.add(id.toString())
            prefs[favoritesKey] = current
        }
    }

    suspend fun removeFavorite(id: Long) {
        dataStore.edit { prefs ->
            val current = prefs[favoritesKey].orEmpty().toMutableSet()
            current.remove(id.toString())
            prefs[favoritesKey] = current
        }
    }
}



