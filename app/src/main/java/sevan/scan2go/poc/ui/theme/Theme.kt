package sevan.scan2go.poc.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = SevanGreen,
    secondary = SevanGreen,
    tertiary = SevanRed,
    background = BackgroundGray,
    surface = White,
    onPrimary = White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = SevanGreen,
    secondary = SevanGreen,
    tertiary = SevanRed,
    background = BackgroundGray,
    surface = White,
    onPrimary = White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun SevanScanToGoPoCTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    // SIDE EFFECT: Force Status Bar Colors
    // Since we are forcing a specific brand look (Green Header) regardless of
    // the user's system Dark Mode settings, we manually override the Window
    // insets to ensure status bar icons remain visible (Light/White).
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            window.statusBarColor = SevanGreen.toArgb()

            window.navigationBarColor = Color.White.toArgb()

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false

            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}