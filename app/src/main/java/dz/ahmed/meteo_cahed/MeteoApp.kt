package dz.ahmed.meteo_cahed

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import dz.ahmed.meteo_cahed.data.datasource.LocalWeatherDataSource
import dz.ahmed.meteo_cahed.data.datasource.RemoteWeatherDataSource
import dz.ahmed.meteo_cahed.data.datasource.UserPreferencesDataSource
import dz.ahmed.meteo_cahed.data.location.DefaultLocationClient
import dz.ahmed.meteo_cahed.data.location.LocationClient
import dz.ahmed.meteo_cahed.data.local.WeatherDatabase
import dz.ahmed.meteo_cahed.data.remote.WeatherApiService
import dz.ahmed.meteo_cahed.data.repository.WeatherRepository
import dz.ahmed.meteo_cahed.data.repository.WeatherRepositoryImpl
import java.time.Duration
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "weather_preferences"
)

class MeteoApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}

interface AppContainer {
    val repository: WeatherRepository
    val locationClient: LocationClient
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val dataStore: DataStore<Preferences> by lazy { context.userPreferencesDataStore }

    private val database: WeatherDatabase by lazy {
        Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather-db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    private val apiService: WeatherApiService by lazy { provideRetrofit().create(WeatherApiService::class.java) }

    private val remoteDataSource: RemoteWeatherDataSource by lazy {
        RemoteWeatherDataSource(apiService)
    }

    private val localDataSource: LocalWeatherDataSource by lazy {
        LocalWeatherDataSource(
            favoriteCityDao = database.favoriteCityDao(),
            weatherDao = database.weatherDao()
        )
    }

    private val preferencesDataSource: UserPreferencesDataSource by lazy {
        UserPreferencesDataSource(dataStore)
    }

    override val repository: WeatherRepository by lazy {
        WeatherRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            userPreferencesDataSource = preferencesDataSource,
            ioDispatcher = Dispatchers.IO
        )
    }

    override val locationClient: LocationClient by lazy {
        DefaultLocationClient(context)
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun provideRetrofit(): Retrofit {
        val networkJson = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(Duration.ofSeconds(15))
            .readTimeout(Duration.ofSeconds(15))
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .client(client)
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}

