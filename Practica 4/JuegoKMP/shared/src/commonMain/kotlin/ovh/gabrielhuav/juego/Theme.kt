package ovh.gabrielhuav.juego

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

val GuindaPrimary = Color(0xFF722F37)
val GuindaOnPrimary = Color(0xFFFFFFFF)
val GuindaBackground = Color(0xFFFDF8F8)
val GuindaSurface = Color(0xFFFFFFFF)
val GuindaOnBackground = Color(0xFF1C1B1B)
val GuindaOnSurface = Color(0xFF1C1B1B)
val GuindaPrimaryDark = Color(0xFFFFB3B8)
val GuindaOnPrimaryDark = Color(0xFF4A1017)
val GuindaBackgroundDark = Color(0xFF1C1B1B)
val GuindaSurfaceDark = Color(0xFF2D2020)
val GuindaOnBackgroundDark = Color(0xFFE6E1E1)
val GuindaOnSurfaceDark = Color(0xFFE6E1E1)

val AzulPrimary = Color(0xFF003F87)
val AzulOnPrimary = Color(0xFFFFFFFF)
val AzulBackground = Color(0xFFF8F9FF)
val AzulSurface = Color(0xFFFFFFFF)
val AzulOnBackground = Color(0xFF1A1C1E)
val AzulOnSurface = Color(0xFF1A1C1E)
val AzulPrimaryDark = Color(0xFFAFC6FF)
val AzulOnPrimaryDark = Color(0xFF00214D)
val AzulBackgroundDark = Color(0xFF1A1C1E)
val AzulSurfaceDark = Color(0xFF1A2030)
val AzulOnBackgroundDark = Color(0xFFE2E2E6)
val AzulOnSurfaceDark = Color(0xFFE2E2E6)

enum class AppTheme { GUINDA, AZUL }

@Composable
fun JuegoTheme(
    appTheme: AppTheme = AppTheme.GUINDA,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        AppTheme.GUINDA -> if (darkTheme) darkColorScheme(
            primary = GuindaPrimaryDark, onPrimary = GuindaOnPrimaryDark,
            background = GuindaBackgroundDark, surface = GuindaSurfaceDark,
            onBackground = GuindaOnBackgroundDark, onSurface = GuindaOnSurfaceDark,
        ) else lightColorScheme(
            primary = GuindaPrimary, onPrimary = GuindaOnPrimary,
            background = GuindaBackground, surface = GuindaSurface,
            onBackground = GuindaOnBackground, onSurface = GuindaOnSurface,
        )
        AppTheme.AZUL -> if (darkTheme) darkColorScheme(
            primary = AzulPrimaryDark, onPrimary = AzulOnPrimaryDark,
            background = AzulBackgroundDark, surface = AzulSurfaceDark,
            onBackground = AzulOnBackgroundDark, onSurface = AzulOnSurfaceDark,
        ) else lightColorScheme(
            primary = AzulPrimary, onPrimary = AzulOnPrimary,
            background = AzulBackground, surface = AzulSurface,
            onBackground = AzulOnBackground, onSurface = AzulOnSurface,
        )
    }
    MaterialTheme(colorScheme = colorScheme, content = content)
}
