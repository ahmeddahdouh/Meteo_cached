package dz.ahmed.meteo_cahed.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onPrimary = DarkOnPrimary,
    onSecondary = DarkOnSecondary,
    onTertiary = DarkOnTertiary,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = LightSecondary,
    tertiary = LightTertiary,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = LightOnPrimary,
    onSecondary = LightOnSecondary,
    onTertiary = LightOnTertiary,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface
)

@Composable
fun Meteo_cahedTheme(
    content: @Composable () -> Unit
) {
    val themeState = rememberDayNightTheme()
    val isDay = themeState.isDay

    val colorScheme = if (isDay) {
        lightColorScheme(
            primary = DayColorPalette.Primary,
            secondary = DayColorPalette.Secondary,
            tertiary = DayColorPalette.Tertiary,
            background = DayColorPalette.Background,
            surface = DayColorPalette.Surface,
            onPrimary = DayColorPalette.OnPrimary,
            onSecondary = DayColorPalette.OnSecondary,
            onTertiary = DayColorPalette.OnTertiary,
            onBackground = DayColorPalette.OnBackground,
            onSurface = DayColorPalette.OnSurface,
            surfaceVariant = DayColorPalette.SurfaceVariant,
            onSurfaceVariant = DayColorPalette.OnSurfaceVariant,
            error = DayColorPalette.Error,
            onError = DayColorPalette.OnError
        )
    } else {
        darkColorScheme(
            primary = NightColorPalette.Primary,
            secondary = NightColorPalette.Secondary,
            tertiary = NightColorPalette.Tertiary,
            background = NightColorPalette.Background,
            surface = NightColorPalette.Surface,
            onPrimary = NightColorPalette.OnPrimary,
            onSecondary = NightColorPalette.OnSecondary,
            onTertiary = NightColorPalette.OnTertiary,
            onBackground = NightColorPalette.OnBackground,
            onSurface = NightColorPalette.OnSurface,
            surfaceVariant = NightColorPalette.SurfaceVariant,
            onSurfaceVariant = NightColorPalette.OnSurfaceVariant,
            error = NightColorPalette.Error,
            onError = NightColorPalette.OnError
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isDay
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
