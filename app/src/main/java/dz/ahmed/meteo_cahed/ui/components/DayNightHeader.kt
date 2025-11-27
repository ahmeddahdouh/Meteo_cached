package dz.ahmed.meteo_cahed.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dz.ahmed.meteo_cahed.ui.theme.DayColorPalette
import dz.ahmed.meteo_cahed.ui.theme.NightColorPalette
import dz.ahmed.meteo_cahed.ui.theme.rememberDayNightTheme

/**
 * En-tête avec icône soleil/lune animée selon le moment de la journée
 */
@Composable
fun DayNightHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    val themeState = rememberDayNightTheme()
    val isDay = themeState.isDay
    
    // Animation de transition soleil -> lune
    val sunScale by animateFloatAsState(
        targetValue = if (isDay) 1f else 0f,
        animationSpec = tween(durationMillis = themeState.transitionDuration),
        label = "sun_scale"
    )
    
    val moonScale by animateFloatAsState(
        targetValue = if (isDay) 0f else 1f,
        animationSpec = tween(durationMillis = themeState.transitionDuration),
        label = "moon_scale"
    )
    
    val sunAlpha by animateFloatAsState(
        targetValue = if (isDay) 1f else 0f,
        animationSpec = tween(durationMillis = themeState.transitionDuration),
        label = "sun_alpha"
    )
    
    val moonAlpha by animateFloatAsState(
        targetValue = if (isDay) 0f else 1f,
        animationSpec = tween(durationMillis = themeState.transitionDuration),
        label = "moon_alpha"
    )
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isDay) "Bonne journée ☀️" else "Bonne soirée 🌙",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // Conteneur pour soleil et lune avec transition
        Box(
            modifier = Modifier.size(64.dp),
            contentAlignment = Alignment.Center
        ) {
            // Soleil (mode jour)
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .scale(sunScale)
                    .alpha(sunAlpha)
                    .background(
                        brush = Brush.radialGradient(
                            listOf(
                                DayColorPalette.SunnyGradientStart,
                                DayColorPalette.SunnyGradientEnd
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "☀️",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 32.sp
                )
            }
            
            // Lune (mode nuit)
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .scale(moonScale)
                    .alpha(moonAlpha)
                    .background(
                        brush = Brush.radialGradient(
                            listOf(
                                NightColorPalette.MoonGradientStart,
                                NightColorPalette.MoonGradientEnd
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🌙",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 32.sp
                )
            }
        }
    }
}



