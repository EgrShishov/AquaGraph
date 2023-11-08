package com.example.aquagraphapp.ui.theme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,

    background = Color(0xFF1a1c1e),
    surface = Color(0xFF1a1c1e),
    onPrimary = Color(0xff003257),
    onSecondary = Color(0xff253140),
    onTertiary = Color(0xff00344b),
    onBackground = Color(0xffe2e2e6),
    onSurface = Color(0xFFe2e2e6),
    primaryContainer = Color(0xff00497c),
    secondaryContainer = Color(0xff3b4858),
    tertiaryContainer = Color(0xff004c6a),
    onPrimaryContainer = Color(0xffd1e4ff),
    onSecondaryContainer = Color(0xffd6e4f7),
    onTertiaryContainer = Color(0xffc5e7ff),
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,

    //Other default colors to override
    background = Color(0xFFfdfcff),
    surface = Color(0xFFfdfcff),
    onPrimary = Color(0xffffffff),
    onSecondary = Color(0xffffffff),
    onTertiary = Color(0xffffffff),
    onBackground = Color(0xff1a1c1e),
    onSurface = Color(0xFF1a1c1e),
    primaryContainer = Color(0xffd1e4ff),
    secondaryContainer = Color(0xffd6e4f7),
    tertiaryContainer = Color(0xffc5e7ff),
    onPrimaryContainer = Color(0xff001d35),
    onSecondaryContainer = Color(0xff0f1c2b),
    onTertiaryContainer = Color(0xff001e2d),
)

@Composable
fun AquaGraphAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}