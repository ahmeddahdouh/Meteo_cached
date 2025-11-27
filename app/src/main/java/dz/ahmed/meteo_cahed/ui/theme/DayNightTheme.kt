package dz.ahmed.meteo_cahed.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import java.util.Calendar

/**
 * Détecte si c'est le jour ou la nuit basé sur l'heure actuelle
 * Jour : 6h - 20h
 * Nuit : 20h - 6h
 */
fun isDayTime(): Boolean {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return hour in 6..19
}

/**
 * Palette de couleurs pour le mode Jour
 */
object DayColorPalette {
    // Fond
    val Background = Color(0xF0FAF9F9) // Gris très clair, presque blanc
    val Surface = Color(0xFFFFFFFF) // Blanc pur
    val SurfaceVariant = Color(0xFFE8ECF1) // Gris clair

    // Primaire - Bleu ciel doux
    val Primary = Color(0xFF4A90E2) // Bleu ciel
    val PrimaryVariant = Color(0xFF357ABD) // Bleu plus foncé
    val OnPrimary = Color(0xFFFFFFFF) // Blanc

    // Secondaire - Jaune soleil
    val Secondary = Color(0xFFFFD93D) // Jaune soleil
    val SecondaryVariant = Color(0xFFFFC107) // Jaune doré
    val OnSecondary = Color(0xFF1A1A1A) // Noir

    // Accent - Orange doux
    val Tertiary = Color(0xFFFF8A65) // Orange doux
    val OnTertiary = Color(0xFFFFFFFF)

    // Texte
    val OnBackground = Color(0xFF1A1A1A) // Noir
    val OnSurface = Color(0xFF1A1A1A) // Noir
    val OnSurfaceVariant = Color(0xFF5A5A5A) // Gris moyen

    // États
    val Error = Color(0xFFE53935)
    val OnError = Color(0xFFFFFFFF)

    // Couleurs spéciales météo
    val SunnyGradientStart = Color(0xFFFFE082) // Jaune clair
    val SunnyGradientEnd = Color(0xFFFFD54F) // Jaune
    val CloudyGradientStart = Color(0xFFE3F2FD) // Bleu très clair
    val CloudyGradientEnd = Color(0xFFBBDEFB) // Bleu clair
    val RainyGradientStart = Color(0xFF90CAF9) // Bleu
    val RainyGradientEnd = Color(0xFF64B5F6) // Bleu plus foncé
    val NightGradientStart = Color(0xFFB39DDB) // Violet clair
    val NightGradientEnd = Color(0xFF9575CD) // Violet
}

/**
 * Palette de couleurs pour le mode Nuit
 */
object NightColorPalette {
    // Fond
    val Background = Color(0xFF0A0E27) // Bleu nuit très sombre
    val Surface = Color(0xFF1A1F3A) // Bleu nuit moyen
    val SurfaceVariant = Color(0xFF252B45) // Bleu nuit clair

    // Primaire - Cyan doux
    val Primary = Color(0xFF64FFDA) // Cyan lumineux
    val PrimaryVariant = Color(0xFF4DD0E1) // Cyan
    val OnPrimary = Color(0xFF0A0E27) // Fond sombre

    // Secondaire - Violet doux
    val Secondary = Color(0xFFB388FF) // Violet lumineux
    val SecondaryVariant = Color(0xFF9575CD) // Violet
    val OnSecondary = Color(0xFFFFFFFF) // Blanc

    // Accent - Rose/Magenta
    val Tertiary = Color(0xFFFF6B9D) // Rose
    val OnTertiary = Color(0xFFFFFFFF)

    // Texte
    val OnBackground = Color(0xFFE8EAF6) // Blanc cassé
    val OnSurface = Color(0xFFE8EAF6) // Blanc cassé
    val OnSurfaceVariant = Color(0xFFB0BEC5) // Gris clair

    // États
    val Error = Color(0xFFFF5252)
    val OnError = Color(0xFFFFFFFF)

    // Couleurs spéciales météo
    val MoonGradientStart = Color(0xFFE1BEE7) // Violet très clair
    val MoonGradientEnd = Color(0xFFCE93D8) // Violet clair
    val CloudyGradientStart = Color(0xFF37474F) // Gris bleu sombre
    val CloudyGradientEnd = Color(0xFF263238) // Gris très sombre
    val RainyGradientStart = Color(0xFF546E7A) // Gris bleu
    val RainyGradientEnd = Color(0xFF455A64) // Gris bleu foncé
    val StarGradientStart = Color(0xFFB39DDB) // Violet
    val StarGradientEnd = Color(0xFF9575CD) // Violet foncé
}

/**
 * État du thème jour/nuit
 */
data class DayNightThemeState(
    val isDay: Boolean,
    val transitionDuration: Int = 800
)

/**
 * Gestionnaire de thème jour/nuit avec animations
 */
@Composable
fun rememberDayNightTheme(): DayNightThemeState {
    val isDay = remember { isDayTime() }
    return remember(isDay) {
        DayNightThemeState(isDay = isDay)
    }
}

/**
 * Obtient la palette de couleurs selon le mode jour/nuit
 */
@Composable
fun getDayNightColorScheme(isDay: Boolean): DayNightColorScheme {
    val dayState = rememberDayNightTheme()
    val currentIsDay = dayState.isDay

    // Animation de transition des couleurs
    val background: Color by animateColorAsState(
        targetValue = if (currentIsDay) DayColorPalette.Background else NightColorPalette.Background,
        animationSpec = tween(durationMillis = dayState.transitionDuration),
        label = "background"
    )

    val surface: Color by animateColorAsState(
        targetValue = if (currentIsDay) DayColorPalette.Surface else NightColorPalette.Surface,
        animationSpec = tween(durationMillis = dayState.transitionDuration),
        label = "surface"
    )

    val primary: Color by animateColorAsState(
        targetValue = if (currentIsDay) DayColorPalette.Primary else NightColorPalette.Primary,
        animationSpec = tween(durationMillis = dayState.transitionDuration),
        label = "primary"
    )

    val secondary: Color by animateColorAsState(
        targetValue = if (currentIsDay) DayColorPalette.Secondary else NightColorPalette.Secondary,
        animationSpec = tween(durationMillis = dayState.transitionDuration),
        label = "secondary"
    )

    return DayNightColorScheme(
        isDay = currentIsDay,
        background = background,
        surface = surface,
        primary = primary,
        secondary = secondary,
        onBackground = if (currentIsDay) DayColorPalette.OnBackground else NightColorPalette.OnBackground,
        onSurface = if (currentIsDay) DayColorPalette.OnSurface else NightColorPalette.OnSurface,
        onPrimary = if (currentIsDay) DayColorPalette.OnPrimary else NightColorPalette.OnPrimary,
        onSecondary = if (currentIsDay) DayColorPalette.OnSecondary else NightColorPalette.OnSecondary
    )
}

/**
 * Schéma de couleurs jour/nuit
 */
data class DayNightColorScheme(
    val isDay: Boolean,
    val background: Color,
    val surface: Color,
    val primary: Color,
    val secondary: Color,
    val onBackground: Color,
    val onSurface: Color,
    val onPrimary: Color,
    val onSecondary: Color
)

