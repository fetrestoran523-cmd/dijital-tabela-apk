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
    primary = SignageBlueLight,
    onPrimary = Color.Black,
    primaryContainer = SignageBlueDark,
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF38BDF8),
    onSecondary = Color.Black,
    background = DarkBackground,
    onBackground = Color(0xFFF1F5F9),
    surface = DarkSurface,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = DarkOutline
  )

private val LightColorScheme =
  lightColorScheme(
    primary = SignageBluePrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = SignageNavy,
    onSecondary = Color.White,
    background = SignageBackgroundLight,
    onBackground = Color(0xFF0F172A),
    surface = SignageSurfaceLight,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = SignageContainerLight,
    onSurfaceVariant = Color(0xFF475569),
    outline = SignageBorderLight
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

