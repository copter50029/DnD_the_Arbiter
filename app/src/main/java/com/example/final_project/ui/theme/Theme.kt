package com.example.final_project.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Dark Grimoire Scheme (Default Ancient Tome immersion)
private val DarkGrimoireColorScheme = darkColorScheme(
    primary = ArcaneGold,
    onPrimary = DarkLeatherDeep,
    primaryContainer = ArcaneGoldContainer,
    onPrimaryContainer = ArcaneGoldBright,

    secondary = CrimsonBright,
    onSecondary = ParchmentLight,
    secondaryContainer = CrimsonContainer,
    onSecondaryContainer = ArcaneGoldBright,

    tertiary = ArcaneGoldBright,
    onTertiary = DarkLeatherDeep,
    tertiaryContainer = DarkLeatherCard,
    onTertiaryContainer = ParchmentLight,

    background = DarkLeatherDeep,
    onBackground = ParchmentLight,

    surface = DarkLeatherSurface,
    onSurface = ParchmentLight,
    surfaceVariant = DarkLeatherCard,
    onSurfaceVariant = ParchmentAged,

    outline = DarkLeatherBorder,
    outlineVariant = ArcaneGoldDark,

    error = RuneErrorBlood,
    onError = ParchmentLight
)

// Light Parchment Scheme (Aged Scroll / Manuscript)
private val LightParchmentColorScheme = lightColorScheme(
    primary = DeepCrimson,
    onPrimary = ParchmentLight,
    primaryContainer = ParchmentDark,
    onPrimaryContainer = DeepCrimson,

    secondary = ArcaneGoldDark,
    onSecondary = ParchmentLight,
    secondaryContainer = ParchmentAged,
    onSecondaryContainer = AncientInk,

    tertiary = CrimsonBright,
    onTertiary = ParchmentLight,
    tertiaryContainer = ParchmentDark,
    onTertiaryContainer = AncientInk,

    background = ParchmentLight,
    onBackground = AncientInk,

    surface = ParchmentAged,
    onSurface = AncientInk,
    surfaceVariant = ParchmentDark,
    onSurfaceVariant = AncientInkMuted,

    outline = ParchmentBorder,
    outlineVariant = ArcaneGoldDark,

    error = RuneErrorBlood,
    onError = ParchmentLight
)

@Composable
fun ArbiterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We intentionally enforce the bespoke Medieval Grimoire / Parchment palette
    // rather than generic dynamic wallpaper colors to preserve immersive aesthetics.
    val colorScheme = if (darkTheme) DarkGrimoireColorScheme else LightParchmentColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}