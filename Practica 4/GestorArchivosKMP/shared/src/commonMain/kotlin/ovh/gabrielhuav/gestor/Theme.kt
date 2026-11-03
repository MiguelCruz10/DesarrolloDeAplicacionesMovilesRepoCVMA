package ovh.gabrielhuav.gestor

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

// ============ COLORES TEMA GUINDA (IPN) ============
val GuindaPrimary = Color(0xFF722F37)
val GuindaOnPrimary = Color(0xFFFFFFFF)
val GuindaSecondary = Color(0xFF9B4D55)
val GuindaTertiary = Color(0xFFB56B73)
val GuindaBackground = Color(0xFFFDF8F8)
val GuindaSurface = Color(0xFFFFFFFF)
val GuindaOnBackground = Color(0xFF1C1B1B)
val GuindaOnSurface = Color(0xFF1C1B1B)

val GuindaPrimaryDark = Color(0xFFFFB3B8)
val GuindaOnPrimaryDark = Color(0xFF4A1017)
val GuindaSecondaryDark = Color(0xFFFFB3BA)
val GuindaTertiaryDark = Color(0xFFFFB3BA)
val GuindaBackgroundDark = Color(0xFF1C1B1B)
val GuindaSurfaceDark = Color(0xFF2D2020)
val GuindaOnBackgroundDark = Color(0xFFE6E1E1)
val GuindaOnSurfaceDark = Color(0xFFE6E1E1)

// ============ COLORES TEMA AZUL (ESCOM) ============
val AzulPrimary = Color(0xFF003F87)
val AzulOnPrimary = Color(0xFFFFFFFF)
val AzulSecondary = Color(0xFF1565C0)
val AzulTertiary = Color(0xFF1976D2)
val AzulBackground = Color(0xFFF8F9FF)
val AzulSurface = Color(0xFFFFFFFF)
val AzulOnBackground = Color(0xFF1A1C1E)
val AzulOnSurface = Color(0xFF1A1C1E)

val AzulPrimaryDark = Color(0xFFAFC6FF)
val AzulOnPrimaryDark = Color(0xFF00214D)
val AzulSecondaryDark = Color(0xFFAFC6FF)
val AzulTertiaryDark = Color(0xFF90CAF9)
val AzulBackgroundDark = Color(0xFF1A1C1E)
val AzulSurfaceDark = Color(0xFF1A2030)
val AzulOnBackgroundDark = Color(0xFFE2E2E6)
val AzulOnSurfaceDark = Color(0xFFE2E2E6)

enum class AppTheme { GUINDA, AZUL }

val LocalAppTheme = compositionLocalOf { AppTheme.GUINDA }

@Composable
fun GestorTheme(
    appTheme: AppTheme = AppTheme.GUINDA,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        AppTheme.GUINDA -> if (darkTheme) darkColorScheme(
            primary = GuindaPrimaryDark,
            onPrimary = GuindaOnPrimaryDark,
            secondary = GuindaSecondaryDark,
            tertiary = GuindaTertiaryDark,
            background = GuindaBackgroundDark,
            surface = GuindaSurfaceDark,
            onBackground = GuindaOnBackgroundDark,
            onSurface = GuindaOnSurfaceDark,
        ) else lightColorScheme(
            primary = GuindaPrimary,
            onPrimary = GuindaOnPrimary,
            secondary = GuindaSecondary,
            tertiary = GuindaTertiary,
            background = GuindaBackground,
            surface = GuindaSurface,
            onBackground = GuindaOnBackground,
            onSurface = GuindaOnSurface,
        )
        AppTheme.AZUL -> if (darkTheme) darkColorScheme(
            primary = AzulPrimaryDark,
            onPrimary = AzulOnPrimaryDark,
            secondary = AzulSecondaryDark,
            tertiary = AzulTertiaryDark,
            background = AzulBackgroundDark,
            surface = AzulSurfaceDark,
            onBackground = AzulOnBackgroundDark,
            onSurface = AzulOnSurfaceDark,
        ) else lightColorScheme(
            primary = AzulPrimary,
            onPrimary = AzulOnPrimary,
            secondary = AzulSecondary,
            tertiary = AzulTertiary,
            background = AzulBackground,
            surface = AzulSurface,
            onBackground = AzulOnBackground,
            onSurface = AzulOnSurface,
        )
    }
    CompositionLocalProvider(LocalAppTheme provides appTheme) {
        MaterialTheme(colorScheme = colorScheme, content = content)
    }
}
