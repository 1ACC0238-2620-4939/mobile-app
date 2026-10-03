package pe.edu.upc.traktoroute.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TraktoColors = lightColorScheme(
    primary = Color(0xFF0B5ED7),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCE8FF),
    secondary = Color(0xFF0E7490),
    background = Color(0xFFF5F7FB),
    surface = Color.White,
    onSurface = Color(0xFF172033),
    error = Color(0xFFB42318),
)

@Composable
fun TraktoTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = TraktoColors, typography = Typography(), content = content)
}
