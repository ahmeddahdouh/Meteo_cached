package dz.ahmed.meteo_cahed.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dz.ahmed.meteo_cahed.data.model.WeatherIcon
import dz.ahmed.meteo_cahed.ui.theme.DayColorPalette
import dz.ahmed.meteo_cahed.ui.theme.NightColorPalette
import dz.ahmed.meteo_cahed.ui.theme.rememberDayNightTheme

/**
 * Icône météo adaptative jour/nuit avec animations
 */
@Composable
fun AdaptiveWeatherIcon(
    icon: WeatherIcon,
    size: Dp = 64.dp,
    modifier: Modifier = Modifier
) {
    val themeState = rememberDayNightTheme()
    val isDay = themeState.isDay
    
    val (emoji, gradientColors, rotation) = when (icon) {
        WeatherIcon.SUNNY -> {
            if (isDay) {
                Triple("☀️", listOf(DayColorPalette.SunnyGradientStart, DayColorPalette.SunnyGradientEnd), 0f)
            } else {
                Triple("🌙", listOf(NightColorPalette.MoonGradientStart, NightColorPalette.MoonGradientEnd), 0f)
            }
        }
        WeatherIcon.CLOUDY -> {
            if (isDay) {
                Triple("⛅", listOf(DayColorPalette.CloudyGradientStart, DayColorPalette.CloudyGradientEnd), 0f)
            } else {
                Triple("☁️", listOf(NightColorPalette.CloudyGradientStart, NightColorPalette.CloudyGradientEnd), 0f)
            }
        }
        WeatherIcon.RAINY -> {
            Triple("🌧️", 
                if (isDay) listOf(DayColorPalette.RainyGradientStart, DayColorPalette.RainyGradientEnd)
                else listOf(NightColorPalette.RainyGradientStart, NightColorPalette.RainyGradientEnd),
                0f
            )
        }
        WeatherIcon.STORM -> {
            Triple("⛈️", listOf(Color(0xFFFF6B6B), Color(0xFFFF5252)), 0f)
        }
        WeatherIcon.SNOW -> {
            Triple("❄️", listOf(Color(0xFFB3E5FC), Color(0xFF81D4FA)), 0f)
        }
        WeatherIcon.FOG -> {
            Triple("🌫️", listOf(Color(0xFFE0E0E0), Color(0xFFBDBDBD)), 0f)
        }
        WeatherIcon.WINDY -> {
            val windRotation = if (isDay) 15f else -15f
            Triple("💨", listOf(Color(0xFF90CAF9), Color(0xFF64B5F6)), windRotation)
        }
        WeatherIcon.UNKNOWN -> {
            Triple("🌡️", listOf(Color(0xFFD1C4E9), Color(0xFFB39DDB)), 0f)
        }
    }
    
    // Animation de rotation pour le soleil/lune
    val animatedRotation by animateFloatAsState(
        targetValue = rotation,
        animationSpec = tween(durationMillis = 1000),
        label = "rotation"
    )
    
    // Animation de scale pour l'apparition
    val animatedScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 500),
        label = "scale"
    )
    
    Box(
        modifier = modifier
            .size(size)
            .scale(animatedScale)
            .background(
                brush = Brush.radialGradient(gradientColors),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            style = TextStyle(
                fontSize = (size.value * 0.6).sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.rotate(animatedRotation)
        )
    }
}

/**
 * Icône soleil animée (mode jour)
 */
@Composable
fun SunIcon(
    size: Dp = 48.dp,
    modifier: Modifier = Modifier
) {
    val animatedRotation by animateFloatAsState(
        targetValue = 360f,
        animationSpec = tween(durationMillis = 20000, delayMillis = 0),
        label = "sun_rotation"
    )
    
    Box(
        modifier = modifier
            .size(size)
            .background(
                brush = Brush.radialGradient(
                    listOf(DayColorPalette.SunnyGradientStart, DayColorPalette.SunnyGradientEnd)
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "☀️",
            style = TextStyle(fontSize = (size.value * 0.6).sp),
            modifier = Modifier.rotate(animatedRotation)
        )
    }
}

/**
 * Icône lune animée (mode nuit)
 */
@Composable
fun MoonIcon(
    size: Dp = 48.dp,
    modifier: Modifier = Modifier
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 2000),
        label = "moon_alpha"
    )
    
    Box(
        modifier = modifier
            .size(size)
            .alpha(animatedAlpha)
            .background(
                brush = Brush.radialGradient(
                    listOf(NightColorPalette.MoonGradientStart, NightColorPalette.MoonGradientEnd)
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "🌙",
            style = TextStyle(fontSize = (size.value * 0.6).sp)
        )
    }
}

