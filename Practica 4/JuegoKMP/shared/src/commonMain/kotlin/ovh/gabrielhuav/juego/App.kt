package ovh.gabrielhuav.juego

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ======================================================
// ESTADOS DE NAVEGACIÓN
// ======================================================

sealed class Screen {
    object MainMenu : Screen()
    data class Game(val level: Int) : Screen()
    object SavedGames : Screen()
    object Settings : Screen()
}

// ======================================================
// COMPOSABLE RAÍZ
// ======================================================

@Composable
fun App() {
    val prefs = remember { PreferencesRepository() }
    val saveManager = remember { SaveManager() }

    var currentTheme by remember {
        mutableStateOf(
            if (prefs.getString("theme") == "azul") AppTheme.AZUL else AppTheme.GUINDA
        )
    }
    var saveFormat by remember {
        mutableStateOf(
            when (prefs.getString("format")) {
                "xml" -> SaveFormat.XML
                "json" -> SaveFormat.JSON
                else -> SaveFormat.TXT
            }
        )
    }

    JuegoTheme(appTheme = currentTheme) {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.MainMenu) }

        when (val screen = currentScreen) {
            is Screen.MainMenu -> MainMenuScreen(
                currentTheme = currentTheme,
                saveFormat = saveFormat,
                onStartGame = { level -> currentScreen = Screen.Game(level) },
                onViewSaves = { currentScreen = Screen.SavedGames },
                onSettings = { currentScreen = Screen.Settings },
                onThemeToggle = {
                    currentTheme = if (currentTheme == AppTheme.GUINDA) AppTheme.AZUL else AppTheme.GUINDA
                    prefs.setString("theme", if (currentTheme == AppTheme.AZUL) "azul" else "guinda")
                }
            )
            is Screen.Game -> GameScreen(
                level = screen.level,
                saveManager = saveManager,
                saveFormat = saveFormat,
                onBack = { currentScreen = Screen.MainMenu }
            )
            is Screen.SavedGames -> SavedGamesScreen(
                saveManager = saveManager,
                onLoadGame = { state -> currentScreen = Screen.Game(state.level) },
                onBack = { currentScreen = Screen.MainMenu }
            )
            is Screen.Settings -> SettingsScreen(
                currentTheme = currentTheme,
                saveFormat = saveFormat,
                onThemeChange = { theme ->
                    currentTheme = theme
                    prefs.setString("theme", if (theme == AppTheme.AZUL) "azul" else "guinda")
                },
                onFormatChange = { format ->
                    saveFormat = format
                    prefs.setString("format", format.name.lowercase())
                },
                onBack = { currentScreen = Screen.MainMenu }
            )
        }
    }
}

// ======================================================
// MENÚ PRINCIPAL
// ======================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    currentTheme: AppTheme,
    saveFormat: SaveFormat,
    onStartGame: (Int) -> Unit,
    onViewSaves: () -> Unit,
    onSettings: () -> Unit,
    onThemeToggle: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("2048 KMP") },
                actions = {
                    IconButton(onClick = onThemeToggle) {
                        Icon(Icons.Default.Star, "Cambiar tema",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("2048 KMP", style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text("Práctica 4 · Móviles · ESCOM IPN",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Tema: ${if (currentTheme == AppTheme.GUINDA) "Guinda (IPN)" else "Azul (ESCOM)"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Formato guardado: ${saveFormat.name}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(32.dp))
            Button(onClick = { onStartGame(1) }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Nueva partida — Nivel 1 (4×4)")
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = { onStartGame(2) }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Nueva partida — Nivel 2 (5×5)")
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onViewSaves, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.List, null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Partidas guardadas")
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onSettings, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Settings, null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Configuración")
            }
        }
    }
}

// ======================================================
// PANTALLA DE JUEGO
// ======================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    level: Int,
    saveManager: SaveManager,
    saveFormat: SaveFormat,
    onBack: () -> Unit
) {
    var gameState by remember { mutableStateOf(GameEngine.newGame(level)) }
    var startTime by remember { mutableStateOf(currentTimeMillis()) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var saveTag by remember { mutableStateOf("") }
    var showGameOverDialog by remember { mutableStateOf(false) }
    var showWinDialog by remember { mutableStateOf(true) }

    val elapsed = (currentTimeMillis() - startTime) / 1000L

    LaunchedEffect(gameState.isGameOver) {
        if (gameState.isGameOver) showGameOverDialog = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("2048 — Nivel $level") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Regresar",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { showSaveDialog = true }) {
                        Icon(Icons.Default.Done, "Guardar",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                    IconButton(onClick = { gameState = GameEngine.newGame(level); startTime = currentTimeMillis() }) {
                        Icon(Icons.Default.Refresh, "Reiniciar",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Score bar
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ScoreCard("Puntos", "${gameState.score}")
                ScoreCard("Tiempo", formatSeconds(elapsed))
                ScoreCard("Nivel", "$level")
            }
            Spacer(Modifier.height(16.dp))

            // Tablero
            GameBoard(
                board = gameState.board,
                onDirection = { dir ->
                    if (!gameState.isGameOver) {
                        gameState = GameEngine.slide(gameState.copy(elapsedSeconds = elapsed), dir)
                    }
                }
            )
            Spacer(Modifier.height(16.dp))

            // Controles de dirección
            DirectionControls(onDirection = { dir ->
                if (!gameState.isGameOver) {
                    gameState = GameEngine.slide(gameState.copy(elapsedSeconds = elapsed), dir)
                }
            })
        }
    }

    // Diálogo de guardado
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Guardar partida") },
            text = {
                Column {
                    Text("Formato: ${saveFormat.name}")
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = saveTag,
                        onValueChange = { saveTag = it },
                        label = { Text("Etiqueta (opcional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val stateToSave = gameState.copy(
                        elapsedSeconds = elapsed,
                        savedAt = currentTimeMillis(),
                        tag = saveTag
                    )
                    saveManager.saveGame(stateToSave, saveFormat, saveTag)
                    showSaveDialog = false
                    saveTag = ""
                }) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) { Text("Cancelar") }
            }
        )
    }

    // Game Over
    if (showGameOverDialog && gameState.isGameOver) {
        AlertDialog(
            onDismissRequest = { showGameOverDialog = false },
            title = { Text("¡Juego terminado!") },
            text = { Text("Puntuación final: ${gameState.score}") },
            confirmButton = {
                TextButton(onClick = {
                    gameState = GameEngine.newGame(level)
                    startTime = currentTimeMillis()
                    showGameOverDialog = false
                }) { Text("Nueva partida") }
            },
            dismissButton = {
                TextButton(onClick = { showGameOverDialog = false; onBack() }) { Text("Menú") }
            }
        )
    }

    // Win!
    if (gameState.hasWon && showWinDialog) {
        AlertDialog(
            onDismissRequest = { showWinDialog = false },
            title = { Text("¡Ganaste!") },
            text = { Text("¡Llegaste a 2048! Puntuación: ${gameState.score}") },
            confirmButton = {
                TextButton(onClick = { showWinDialog = false }) { Text("Seguir jugando") }
            },
            dismissButton = {
                TextButton(onClick = { showWinDialog = false; onBack() }) { Text("Menú") }
            }
        )
    }
}

@Composable
fun ScoreCard(label: String, value: String) {
    Card(modifier = Modifier.padding(4.dp)) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary)
            Text(label, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun GameBoard(board: List<List<Int>>, onDirection: (Direction) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            board.forEach { row ->
                Row(modifier = Modifier.weight(1f).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    row.forEach { value ->
                        TileCell(value = value, modifier = Modifier.weight(1f).fillMaxSize())
                    }
                }
            }
        }
    }
}

@Composable
fun TileCell(value: Int, modifier: Modifier = Modifier) {
    val (bg, fg) = tileColors(value)
    Box(
        modifier = modifier
            .background(bg, MaterialTheme.shapes.small)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), MaterialTheme.shapes.small),
        contentAlignment = Alignment.Center
    ) {
        if (value != 0) {
            Text(
                text = if (value >= 1000) "${value / 1000}k" else "$value",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (value >= 1000) 12.sp else 16.sp
                ),
                color = fg,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun tileColors(value: Int): Pair<Color, Color> = when (value) {
    0 -> Color(0xFFCDC1B4) to Color.Transparent
    2 -> Color(0xFFEEE4DA) to Color(0xFF776E65)
    4 -> Color(0xFFEDE0C8) to Color(0xFF776E65)
    8 -> Color(0xFFF2B179) to Color.White
    16 -> Color(0xFFF59563) to Color.White
    32 -> Color(0xFFF67C5F) to Color.White
    64 -> Color(0xFFF65E3B) to Color.White
    128 -> Color(0xFFEDCF72) to Color.White
    256 -> Color(0xFFEDCC61) to Color.White
    512 -> Color(0xFFEDC850) to Color.White
    1024 -> Color(0xFFEDC53F) to Color.White
    2048 -> Color(0xFFEDC22E) to Color.White
    else -> Color(0xFF3C3A32) to Color.White
}

@Composable
fun DirectionControls(onDirection: (Direction) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Controles", style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(4.dp))
        IconButton(onClick = { onDirection(Direction.UP) }) {
            Icon(Icons.Default.KeyboardArrowUp, "Arriba",
                modifier = Modifier.size(36.dp), tint = MaterialTheme.colorScheme.primary)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IconButton(onClick = { onDirection(Direction.LEFT) }) {
                Icon(Icons.Default.KeyboardArrowLeft, "Izquierda",
                    modifier = Modifier.size(36.dp), tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = { onDirection(Direction.DOWN) }) {
                Icon(Icons.Default.KeyboardArrowDown, "Abajo",
                    modifier = Modifier.size(36.dp), tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = { onDirection(Direction.RIGHT) }) {
                Icon(Icons.Default.KeyboardArrowRight, "Derecha",
                    modifier = Modifier.size(36.dp), tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

fun formatSeconds(seconds: Long): String {
    val m = seconds / 60
    val s = seconds % 60
    return "${m}m ${s}s"
}

// ======================================================
// PARTIDAS GUARDADAS
// ======================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedGamesScreen(
    saveManager: SaveManager,
    onLoadGame: (GameState) -> Unit,
    onBack: () -> Unit
) {
    var saves by remember { mutableStateOf(saveManager.listSaves()) }
    var showContent by remember { mutableStateOf("") }
    var showContentDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Partidas guardadas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Regresar",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        if (saves.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Warning, null, modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Text("No hay partidas guardadas",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(saves) { save ->
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text(save.saveId, style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold)
                                    if (save.tag.isNotEmpty()) {
                                        Text("🏷 ${save.tag}", style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text(save.format.name, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall)
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("Puntuación: ${save.score} · Nivel: ${save.level} · ${formatSeconds(save.elapsedSeconds)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            if (save.savedAt > 0L) {
                                Text("Guardado: ${formatEpoch(save.savedAt)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                TextButton(onClick = {
                                    val state = saveManager.loadGame(save.saveId)
                                    if (state != null) onLoadGame(state)
                                }) { Text("Cargar") }
                                TextButton(onClick = {
                                    showContent = saveManager.getSaveContent(save.saveId)
                                    showContentDialog = true
                                }) { Text("Ver") }
                                TextButton(onClick = { showDeleteDialog = save.saveId },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error)) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo ver contenido
    if (showContentDialog) {
        AlertDialog(
            onDismissRequest = { showContentDialog = false },
            title = { Text("Contenido del archivo") },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    item {
                        Text(showContent,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showContentDialog = false }) { Text("Cerrar") }
            }
        )
    }

    // Diálogo eliminar
    showDeleteDialog?.let { saveId ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Eliminar partida") },
            text = { Text("¿Deseas eliminar la partida \"$saveId\"?") },
            confirmButton = {
                TextButton(onClick = {
                    saveManager.deleteSave(saveId)
                    saves = saveManager.listSaves()
                    showDeleteDialog = null
                }, colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error)) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) { Text("Cancelar") }
            }
        )
    }
}

// ======================================================
// CONFIGURACIÓN
// ======================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentTheme: AppTheme,
    saveFormat: SaveFormat,
    onThemeChange: (AppTheme) -> Unit,
    onFormatChange: (SaveFormat) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Regresar",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            item {
                Text("Tema de color", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(AppTheme.GUINDA == currentTheme, { onThemeChange(AppTheme.GUINDA) },
                        label = { Text("Guinda (IPN)") })
                    FilterChip(AppTheme.AZUL == currentTheme, { onThemeChange(AppTheme.AZUL) },
                        label = { Text("Azul (ESCOM)") })
                }
                Spacer(Modifier.height(24.dp))
                Text("Formato de guardado", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SaveFormat.values().forEach { fmt ->
                        FilterChip(saveFormat == fmt, { onFormatChange(fmt) },
                            label = { Text(fmt.name) })
                    }
                }
                Spacer(Modifier.height(24.dp))
                Text("Plataforma: ${plataforma()}", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
