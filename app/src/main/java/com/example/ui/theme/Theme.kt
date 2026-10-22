package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AuraCyanBright,
    onPrimary = AuraBackgroundDark,
    primaryContainer = AuraSurfaceElevated,
    onPrimaryContainer = AuraCyanBright,
    secondary = AuraIndigo,
    onSecondary = AuraTextPrimary,
    secondaryContainer = AuraSurfaceElevated,
    onSecondaryContainer = AuraTextPrimary,
    tertiary = AuraAmber,
    onTertiary = AuraBackgroundDark,
    background = AuraBackgroundDark,
    onBackground = AuraTextPrimary,
    surface = AuraSurfaceDark,
    onSurface = AuraTextPrimary,
    surfaceVariant = AuraSurfaceElevated,
    onSurfaceVariant = AuraTextSecondary,
    outline = AuraCardBorder,
    error = AuraRoseUrgent,
    onError = AuraTextPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = AuraCyan,
    onPrimary = AuraTextPrimary,
    primaryContainer = AuraSurfaceLightElevated,
    onPrimaryContainer = AuraCyan,
    secondary = AuraIndigo,
    onSecondary = AuraTextPrimary,
    tertiary = AuraAmber,
    background = AuraBackgroundLight,
    onBackground = AuraTextLightPrimary,
    surface = AuraSurfaceLight,
    onSurface = AuraTextLightPrimary,
    surfaceVariant = AuraSurfaceLightElevated,
    onSurfaceVariant = AuraTextLightSecondary,
    outline = AuraCardBorder,
    error = AuraRoseUrgent,
    onError = AuraTextPrimary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek executive dark mode
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
