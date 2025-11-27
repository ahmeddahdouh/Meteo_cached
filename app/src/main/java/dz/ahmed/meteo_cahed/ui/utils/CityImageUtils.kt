package dz.ahmed.meteo_cahed.ui.utils

import dz.ahmed.meteo_cahed.data.model.City
import dz.ahmed.meteo_cahed.data.model.WeatherSummary

/**
 * Génère une URL d'image pour une ville via Unsplash
 * Utilise le nom de la ville pour chercher une photo appropriée
 */
fun getCityImageUrl(cityName: String, width: Int = 800, height: Int = 600): String {
    val query = cityName.replace(" ", "+")
    // Unsplash Source API (gratuite, pas besoin de clé API)
    return "https://source.unsplash.com/featured/${width}x${height}/?city,$query&sig=${cityName.hashCode()}"
}

/**
 * Alternative: utiliser Unsplash API avec une clé (si disponible)
 * Pour l'instant, on utilise source.unsplash.com qui est gratuit
 */
fun getCityImageUrlFromCity(city: City): String = getCityImageUrl(city.name)

fun getCityImageUrlFromSummary(summary: WeatherSummary): String = getCityImageUrl(summary.cityName)



