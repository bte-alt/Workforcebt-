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

private val DarkColorScheme =
  darkColorScheme(
    primary = TealLight,
    onPrimary = Color(0xFF003833),
    primaryContainer = TealDark,
    onPrimaryContainer = TealContainer,
    secondary = AmberAccent,
    onSecondary = Color(0xFF452B00),
    secondaryContainer = AmberDark,
    onSecondaryContainer = AmberContainer,
    tertiary = IndigoAccent,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    outline = DarkOutline
  )

private val LightColorScheme =
  lightColorScheme(
    primary = TealDark,
    onPrimary = Color.White,
    primaryContainer = TealContainer,
    onPrimaryContainer = OnTealContainer,
    secondary = AmberDark,
    onSecondary = Color.White,
    secondaryContainer = AmberContainer,
    onSecondaryContainer = OnAmberContainer,
    tertiary = IndigoAccent,
    background = SlateBackground,
    surface = SlateSurface,
    surfaceVariant = SlateSurfaceVariant,
    onBackground = SlateTextPrimary,
    onSurface = SlateTextPrimary,
    outline = SlateOutline
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our branded theme for consistent marketplace identity
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
