package ovh.gabrielhuav.flasklogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ovh.gabrielhuav.flasklogin.ui.*
import ovh.gabrielhuav.flasklogin.ui.theme.FlaskLoginTheme

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Section1 : Screen("s1", "Textos", Icons.Default.TextFields)
    object Section2 : Screen("s2", "Botones", Icons.Default.SmartButton)
    object Section3 : Screen("s3", "Selección", Icons.Default.CheckBox)
    object Section4 : Screen("s4", "Listas", Icons.Default.FormatListBulleted)
    object Section5 : Screen("s5", "Feedback", Icons.Default.Feedback)
    object Section6 : Screen("s6", "Estructura", Icons.Default.DashboardCustomize)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkThemeEnabled by remember { mutableStateOf(false) }

            FlaskLoginTheme(darkTheme = darkThemeEnabled) {
                MainCatalogoComposeApp(
                    isDark = darkThemeEnabled,
                    onToggleTheme = { darkThemeEnabled = !darkThemeEnabled }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCatalogoComposeApp(
    isDark: Boolean,
    onToggleTheme: () -> Unit
) {
    val screens = listOf(
        Screen.Section1,
        Screen.Section2,
        Screen.Section3,
        Screen.Section4,
        Screen.Section5,
        Screen.Section6
    )

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Section1) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                Text(
                    "Catálogo Compose",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    "Usuario: ${AppGlobalState.nombreUsuario}\nBoleta: 2024630153 - Grupo: 7CV4",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                screens.forEach { screen ->
                    NavigationDrawerItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentScreen == screen,
                        onClick = {
                            currentScreen = screen
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                NavigationDrawerItem(
                    icon = { Icon(if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode, contentDescription = null) },
                    label = { Text(if (isDark) "Cambiar a Modo Claro" else "Cambiar a Modo Oscuro") },
                    selected = false,
                    onClick = {
                        onToggleTheme()
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(currentScreen.title) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = onToggleTheme) {
                            Icon(
                                if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Tema"
                            )
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                NavigationBar {
                    screens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.title) },
                            selected = currentScreen == screen,
                            onClick = { currentScreen = screen }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    Screen.Section1 -> Section1TextScreen()
                    Screen.Section2 -> Section2ButtonsScreen()
                    Screen.Section3 -> Section3SelectionScreen()
                    Screen.Section4 -> Section4ListsScreen()
                    Screen.Section5 -> Section5FeedbackScreen(snackbarHostState)
                    Screen.Section6 -> Section6ContainersScreen()
                }
            }
        }
    }
}