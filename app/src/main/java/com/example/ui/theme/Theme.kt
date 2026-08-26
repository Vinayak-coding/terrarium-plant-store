package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightTerrariumColorScheme = lightColorScheme(
    primary = ForestGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = SoftSageContainer,
    onPrimaryContainer = ForestGreenDark,
    secondary = TerracottaAccent,
    onSecondary = Color.White,
    secondaryContainer = TerracottaLight,
    onSecondaryContainer = TerracottaDark,
    tertiary = FoliageGreen,
    onTertiary = Color.White,
    tertiaryContainer = MintLight,
    onTertiaryContainer = ForestGreenDark,
    background = CreamBackground,
    onBackground = TextDarkGreen,
    surface = SurfaceCream,
    onSurface = TextDarkGreen,
    surfaceVariant = SurfaceCreamVariant,
    onSurfaceVariant = TextMedium,
    outline = SoftBorder,
    outlineVariant = WarmSand,
    error = AlertRed,
    onError = Color.White
)

private val DarkTerrariumColorScheme = darkColorScheme(
    primary = SageGreen,
    onPrimary = ForestGreenDark,
    primaryContainer = ForestGreenLight,
    onPrimaryContainer = MintLight,
    secondary = TerracottaAccent,
    onSecondary = Color.White,
    secondaryContainer = TerracottaDark,
    onSecondaryContainer = TerracottaLight,
    tertiary = MintLight,
    onTertiary = ForestGreenDark,
    background = Color(0xFF141E16),
    onBackground = Color(0xFFE8EFE9),
    surface = Color(0xFF1C2B1F),
    onSurface = Color(0xFFE8EFE9),
    surfaceVariant = Color(0xFF26392B),
    onSurfaceVariant = Color(0xFFBCCBBF),
    outline = Color(0xFF384D3E),
    outlineVariant = Color(0xFF2A3D2F),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun TerrariumTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our handcrafted botanical theme for brand consistency
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkTerrariumColorScheme
        else -> LightTerrariumColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TerrariumTypography,
        content = content
    )
}
