package dz.ahmed.meteo_cahed.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

interface LocationClient {
    suspend fun getCurrentLocation(): LocationResult
}

sealed class LocationResult {
    data class Success(val latitude: Double, val longitude: Double) : LocationResult()
    data class Error(val throwable: Throwable) : LocationResult()
}

sealed class LocationException(message: String) : Exception(message) {
    data object PermissionDenied : LocationException("Autorisation de localisation manquante.")
    data object Timeout : LocationException("Impossible de récupérer la localisation.")
}

class DefaultLocationClient(
    private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
) : LocationClient {

    override suspend fun getCurrentLocation(): LocationResult {
        return if (hasPermission().not()) {
            LocationResult.Error(LocationException.PermissionDenied)
        } else {
            runCatching { awaitLocation() }
                .map { location -> LocationResult.Success(location.latitude, location.longitude) }
                .getOrElse { throwable -> LocationResult.Error(throwable) }
        }
    }

    private fun hasPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    private suspend fun awaitLocation(): Location = suspendCancellableCoroutine { continuation ->
        val cancellationTokenSource = CancellationTokenSource()
        fusedLocationProviderClient.getCurrentLocation(
            com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            cancellationTokenSource.token
        ).apply {
            addOnSuccessListener { location ->
                if (location != null && continuation.isActive) {
                    continuation.resume(location)
                } else if (continuation.isActive) {
                    continuation.resumeWithException(LocationException.Timeout)
                }
            }
            addOnFailureListener { error ->
                if (continuation.isActive) {
                    continuation.resumeWithException(error)
                }
            }
        }

        continuation.invokeOnCancellation {
            cancellationTokenSource.cancel()
            it?.let { throwable ->
                if (throwable !is CancellationException) {
                    throw throwable
                }
            }
        }
    }
}

