package com.lifeos.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = LifeOsPrimary,
    onPrimary = LifeOsOnPrimary,
    primaryContainer = LifeOsPrimaryContainer,
    onPrimaryContainer = LifeOsOnPrimaryContainer,
    secondary = LifeOsSecondary,
    onSecondary = LifeOsOnSecondary,
    secondaryContainer = LifeOsSecondaryContainer,
    onSecondaryContainer = LifeOsOnSecondaryContainer,
    tertiary = LifeOsTertiary,
    onTertiary = LifeOsOnTertiary,
    background = LifeOsBackgroundDark,
    surface = LifeOsSurfaceDark,
    surfaceVariant = LifeOsSurfaceVariantDark,
    onBackground = LifeOsOnBackgroundDark,
    onSurface = LifeOsOnSurfaceDark
)

private val LightColorScheme = lightColorScheme(
    primary = LifeOsPrimary,
    onPrimary = LifeOsOnPrimary,
    primaryContainer = LifeOsOnPrimaryContainer,
    onPrimaryContainer = LifeOsPrimaryContainer,
    secondary = LifeOsSecondary,
    onSecondary = LifeOsOnSecondary,
    tertiary = LifeOsTertiary,
    onTertiary = LifeOsOnTertiary
)

@Composable
fun LifeOsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
