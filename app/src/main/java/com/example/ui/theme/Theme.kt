package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldBull,
    secondary = SecondaryTeal,
    tertiary = CharcoalGold,
    background = ObsidianBg,
    surface = MidnightCard,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = LightText,
    onSurface = LightText,
    error = CrimsonBear
)

// Consistent light slate styling
private val LightColorScheme = lightColorScheme(
    primary = EmeraldBull,
    secondary = SecondaryTeal,
    tertiary = CharcoalGold,
    background = ObsidianBg,
    surface = MidnightCard,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = LightText,
    onSurface = LightText,
    error = CrimsonBear
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Use the new Sleek light-slate theme by default
    dynamicColor: Boolean = false, // Disable dynamic colors so our royal/slate theme is fully preserved
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
