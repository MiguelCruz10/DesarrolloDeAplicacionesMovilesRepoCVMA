package ovh.gabrielhuav.gestor

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

// ======================================================
// ESTADOS DE NAVEGACIÓN
// ======================================================

sealed class Screen {
    object Home : Screen()
    object FileExplorer : Screen()
    object RecentFiles : Screen()
    object Favorites : Screen()
    data class FileViewer(val item: FileItem) : Screen()
    object Settings : Screen()
}

// ======================================================
// COMPOSABLE RAÍZ
// ======================================================

@Composable
fun App() {
    val prefs = remember { PreferencesRepository() }
    val fileRepo = remember { FileRepository() }
    val recentMgr = remember { RecentFilesManager(prefs) }

    var currentTheme by remember {
        mutableStateOf(
            if (prefs.getString("theme") == "azul") AppTheme.AZUL else AppTheme.GUINDA
        )
    }

    GestorTheme(appTheme = currentTheme) {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
        var currentPath by remember { mutableStateOf("") }
        var breadcrumbs by remember { mutableStateOf(listOf<String>()) }

        Scaffold(
            topBar = {
                GestorTopBar(
                    screen = currentScreen,
                    currentTheme = currentTheme,
                    onThemeToggle = {
                        currentTheme = if (currentTheme == AppTheme.GUINDA) AppTheme.AZUL else AppTheme.GUINDA
                        prefs.setString("theme", if (currentTheme == AppTheme.AZUL) "azul" else "guinda")
                    },
                    onNavigateHome = {
                        currentScreen = Screen.Home
                        breadcrumbs = listOf()
                    }
                )
            },
            bottomBar = {
                GestorBottomBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen -> currentScreen = screen }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                when (val screen = currentScreen) {
                    is Screen.Home -> HomeScreen(
                        onExplore = {
                            currentPath = ""
                            breadcrumbs = listOf("Inicio")
                            currentScreen = Screen.FileExplorer
                        },
                        onRecent = { currentScreen = Screen.RecentFiles },
                        onFavorites = { currentScreen = Screen.Favorites },
                        onSettings = { currentScreen = Screen.Settings }
                    )
                    is Screen.FileExplorer -> FileExplorerScreen(
                        fileRepo = fileRepo,
                        recentMgr = recentMgr,
                        currentPath = currentPath,
                        breadcrumbs = breadcrumbs,
                        onNavigate = { item ->
                            if (item.isDirectory) {
                                currentPath = item.path
                                breadcrumbs = breadcrumbs + item.name
                            } else {
                                recentMgr.addRecent(item.path)
                                currentScreen = Screen.FileViewer(item)
                            }
                        },
                        onBreadcrumbClick = { index ->
                            if (index == 0) {
                                currentPath = ""
                                breadcrumbs = listOf("Inicio")
                            }
                        }
                    )
                    is Screen.RecentFiles -> RecentFilesScreen(
                        recentMgr = recentMgr,
                        fileRepo = fileRepo,
                        onOpenFile = { item -> currentScreen = Screen.FileViewer(item) }
                    )
                    is Screen.Favorites -> FavoritesScreen(
                        recentMgr = recentMgr,
                        fileRepo = fileRepo,
                        onOpenFile = { item -> currentScreen = Screen.FileViewer(item) }
                    )
                    is Screen.FileViewer -> FileViewerScreen(
                        item = screen.item,
                        fileRepo = fileRepo,
                        recentMgr = recentMgr,
                        onBack = { currentScreen = Screen.FileExplorer }
                    )
                    is Screen.Settings -> SettingsScreen(
                        currentTheme = currentTheme,
                        onThemeChange = { theme ->
                            currentTheme = theme
                            prefs.setString("theme", if (theme == AppTheme.AZUL) "azul" else "guinda")
                        },
                        onClearRecent = { recentMgr.clearRecent() }
                    )
                }
            }
        }
    }
}

// ======================================================
// TOP APP BAR
// ======================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestorTopBar(
    screen: Screen,
    currentTheme: AppTheme,
    onThemeToggle: () -> Unit,
    onNavigateHome: () -> Unit
) {
    val title = when (screen) {
        is Screen.Home -> "Gestor de Archivos"
        is Screen.FileExplorer -> "Explorador"
        is Screen.RecentFiles -> "Recientes"
        is Screen.Favorites -> "Favoritos"
        is Screen.FileViewer -> screen.item.name
        is Screen.Settings -> "Configuración"
    }
    TopAppBar(
        title = { Text(title, maxLines = 1) },
        navigationIcon = {
            if (screen != Screen.Home) {
                IconButton(onClick = onNavigateHome) {
                    Icon(Icons.Default.Home, contentDescription = "Inicio")
                }
            }
        },
        actions = {
            IconButton(onClick = onThemeToggle) {
                Icon(
                    imageVector = if (currentTheme == AppTheme.GUINDA) Icons.Default.Star else Icons.Default.FavoriteBorder,
                    contentDescription = "Cambiar tema: ${if (currentTheme == AppTheme.GUINDA) "Guinda" else "Azul"}"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        )
    )
}

// ======================================================
// BOTTOM NAVIGATION
// ======================================================

@Composable
fun GestorBottomBar(currentScreen: Screen, onNavigate: (Screen) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, "Inicio") },
            label = { Text("Inicio") },
            selected = currentScreen is Screen.Home,
            onClick = { onNavigate(Screen.Home) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, "Explorar") },
            label = { Text("Explorar") },
            selected = currentScreen is Screen.FileExplorer,
            onClick = { onNavigate(Screen.FileExplorer) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Refresh, "Recientes") },
            label = { Text("Recientes") },
            selected = currentScreen is Screen.RecentFiles,
            onClick = { onNavigate(Screen.RecentFiles) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, "Favoritos") },
            label = { Text("Favoritos") },
            selected = currentScreen is Screen.Favorites,
            onClick = { onNavigate(Screen.Favorites) }
        )
    }
}

// ======================================================
// PANTALLA INICIO
// ======================================================

@Composable
fun HomeScreen(
    onExplore: () -> Unit,
    onRecent: () -> Unit,
    onFavorites: () -> Unit,
    onSettings: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Create,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))
        Text("Gestor de Archivos", style = MaterialTheme.typography.headlineMedium)
        Text("Práctica 4 · Móviles · ESCOM IPN", style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(32.dp))
        HomeButton("Explorar archivos", Icons.Default.Search, onExplore)
        Spacer(Modifier.height(12.dp))
        HomeButton("Archivos recientes", Icons.Default.Refresh, onRecent)
        Spacer(Modifier.height(12.dp))
        HomeButton("Favoritos", Icons.Default.Favorite, onFavorites)
        Spacer(Modifier.height(12.dp))
        HomeButton("Configuración", Icons.Default.Settings, onSettings)
    }
}

@Composable
fun HomeButton(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(label)
    }
}

// ======================================================
// EXPLORADOR DE ARCHIVOS
// ======================================================

@Composable
fun FileExplorerScreen(
    fileRepo: FileRepository,
    recentMgr: RecentFilesManager,
    currentPath: String,
    breadcrumbs: List<String>,
    onNavigate: (FileItem) -> Unit,
    onBreadcrumbClick: (Int) -> Unit
) {
    val files by remember(currentPath) {
        derivedStateOf { fileRepo.listInternalFiles(currentPath) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Breadcrumbs
        if (breadcrumbs.isNotEmpty()) {
            BreadcrumbRow(breadcrumbs, onBreadcrumbClick)
        }

        if (files.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "No hay archivos en este directorio",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn {
                items(files) { item ->
                    FileListItem(
                        item = item,
                        isFavorite = recentMgr.isFavorite(item.path),
                        onToggleFavorite = {
                            if (recentMgr.isFavorite(item.path)) recentMgr.removeFavorite(item.path)
                            else recentMgr.addFavorite(item.path)
                        },
                        onClick = { onNavigate(item) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun BreadcrumbRow(breadcrumbs: List<String>, onBreadcrumbClick: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        breadcrumbs.forEachIndexed { index, crumb ->
            if (index > 0) Text(" › ", color = MaterialTheme.colorScheme.onSurfaceVariant)
            TextButton(
                onClick = { onBreadcrumbClick(index) },
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(crumb, style = MaterialTheme.typography.bodySmall,
                    color = if (index == breadcrumbs.lastIndex) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun FileListItem(
    item: FileItem,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(item.name, maxLines = 1) },
        supportingContent = {
            if (!item.isDirectory) {
                Text(
                    formatFileSize(item.size) + " · " + item.mimeType.ifEmpty { "desconocido" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        leadingContent = {
            Icon(
                imageVector = if (item.isDirectory) Icons.Default.Create else Icons.Default.Menu,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingContent = {
            if (!item.isDirectory) {
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary
                               else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    )
}

fun formatFileSize(bytes: Long): String = when {
    bytes < 0 -> "desconocido"
    bytes < 1024 -> "$bytes B"
    bytes < 1024 * 1024 -> "${bytes / 1024} KB"
    else -> "${bytes / (1024 * 1024)} MB"
}

// ======================================================
// VISOR DE ARCHIVOS
// ======================================================

@Composable
fun FileViewerScreen(
    item: FileItem,
    fileRepo: FileRepository,
    recentMgr: RecentFilesManager,
    onBack: () -> Unit
) {
    val result by remember(item.path) {
        derivedStateOf { fileRepo.readFile(item.path) }
    }
    val metadata = fileRepo.getMetadata(item.path)
    var showMetadata by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Barra de acciones del visor
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, "Regresar")
            }
            Row {
                IconButton(onClick = { showMetadata = !showMetadata }) {
                    Icon(Icons.Default.Info, "Metadatos")
                }
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Default.Delete, "Eliminar")
                }
            }
        }

        if (showMetadata && metadata != null) {
            MetadataCard(metadata)
        }

        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            when (val r = result) {
                is FileReadResult.TextContent -> TextViewer(r.text, item.mimeType)
                is FileReadResult.ImageBytes -> ImageViewer(r.bytes)
                is FileReadResult.UnsupportedFormat -> UnsupportedFormatView(r.mimeType)
                is FileReadResult.Error -> ErrorView(r.message)
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar archivo") },
            text = { Text("¿Deseas eliminar \"${item.name}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    fileRepo.deleteFile(item.path)
                    showDeleteDialog = false
                    onBack()
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun MetadataCard(metadata: FileMetadata) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Metadatos", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(4.dp))
            MetaRow("Nombre", metadata.name)
            MetaRow("Tamaño", formatFileSize(metadata.size))
            MetaRow("Tipo MIME", metadata.mimeType.ifEmpty { "desconocido" })
            MetaRow("Última modificación",
                if (metadata.lastModified == 0L) "no disponible"
                else formatEpoch(metadata.lastModified))
        }
    }
}

@Composable
fun MetaRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text("$label: ", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun TextViewer(text: String, mimeType: String) {
    val isJson = mimeType.contains("json")
    val isXml = mimeType.contains("xml")
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        LazyColumn(modifier = Modifier.padding(12.dp)) {
            val lines = text.lines()
            items(lines.size) { index ->
                val line = lines[index]
                Row {
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.width(32.dp)
                    )
                    Text(
                        text = line,
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                        color = when {
                            isJson && (line.trimStart().startsWith("\"") || line.contains(":")) ->
                                MaterialTheme.colorScheme.primary
                            isXml && line.trimStart().startsWith("<") ->
                                MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ImageViewer(bytes: ByteArray) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.Menu,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Imagen (${formatFileSize(bytes.size.toLong())})",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Vista previa disponible en dispositivo",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun UnsupportedFormatView(mimeType: String) {
    var showDialog by remember { mutableStateOf(true) }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Warning, null, modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Text("Formato no soportado", style = MaterialTheme.typography.titleMedium)
            Text(mimeType.ifEmpty { "tipo desconocido" },
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(16.dp))
            Button(onClick = { showDialog = true }) { Text("Opciones de apertura") }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Abrir archivo") },
            text = { Text("Este formato ($mimeType) no tiene visor integrado. Puedes abrirlo con otra aplicación del sistema.") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) { Text("Entendido") }
            }
        )
    }
}

@Composable
fun ErrorView(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Warning, null, modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
            Text("Error al leer el archivo", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(message, color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall)
        }
    }
}

// ======================================================
// ARCHIVOS RECIENTES
// ======================================================

@Composable
fun RecentFilesScreen(
    recentMgr: RecentFilesManager,
    fileRepo: FileRepository,
    onOpenFile: (FileItem) -> Unit
) {
    val recents = remember { recentMgr.getRecent() }
    if (recents.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Refresh, null, modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                Text("No hay archivos recientes", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    } else {
        LazyColumn {
            items(recents) { path ->
                val meta = fileRepo.getMetadata(path)
                val item = FileItem(
                    name = meta?.name ?: path.substringAfterLast('/'),
                    path = path,
                    isDirectory = meta?.isDirectory ?: false,
                    size = meta?.size ?: 0L,
                    mimeType = meta?.mimeType ?: ""
                )
                ListItem(
                    headlineContent = { Text(meta?.name ?: path.substringAfterLast('/'), maxLines = 1) },
                    supportingContent = {
                        Text(path, maxLines = 1,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    },
                    leadingContent = {
                        Icon(Icons.Default.Menu, null, tint = MaterialTheme.colorScheme.primary)
                    },
                    modifier = Modifier.fillMaxWidth().clickable { onOpenFile(item) }
                )
                HorizontalDivider()
            }
        }
    }
}

// ======================================================
// FAVORITOS
// ======================================================

@Composable
fun FavoritesScreen(
    recentMgr: RecentFilesManager,
    fileRepo: FileRepository,
    onOpenFile: (FileItem) -> Unit
) {
    val favorites = remember { recentMgr.getFavorites() }
    if (favorites.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Favorite, null, modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                Text("No hay favoritos guardados", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    } else {
        LazyColumn {
            items(favorites) { path ->
                val meta = fileRepo.getMetadata(path)
                val item = FileItem(
                    name = meta?.name ?: path.substringAfterLast('/'),
                    path = path,
                    isDirectory = meta?.isDirectory ?: false,
                    size = meta?.size ?: 0L,
                    mimeType = meta?.mimeType ?: ""
                )
                ListItem(
                    headlineContent = { Text(meta?.name ?: path.substringAfterLast('/'), maxLines = 1) },
                    supportingContent = {
                        Text(path, maxLines = 1,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    },
                    leadingContent = {
                        Icon(Icons.Default.Favorite, null, tint = MaterialTheme.colorScheme.primary)
                    },
                    modifier = Modifier.fillMaxWidth().clickable { onOpenFile(item) }
                )
                HorizontalDivider()
            }
        }
    }
}

// ======================================================
// CONFIGURACIÓN
// ======================================================

@Composable
fun SettingsScreen(
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
    onClearRecent: () -> Unit
) {
    var showClearDialog by remember { mutableStateOf(false) }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Tema de color", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ThemeChip("Guinda (IPN)", currentTheme == AppTheme.GUINDA) { onThemeChange(AppTheme.GUINDA) }
                ThemeChip("Azul (ESCOM)", currentTheme == AppTheme.AZUL) { onThemeChange(AppTheme.AZUL) }
            }
            Spacer(Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Acerca de", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(4.dp))
                    Text("Gestor de Archivos KMP", style = MaterialTheme.typography.bodyMedium)
                    Text("Práctica 4 · Móviles · ESCOM IPN", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Plataforma: ${plataforma()}", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(Modifier.height(24.dp))
            Text("Gestión de datos", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = { showClearDialog = true }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Delete, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Limpiar archivos recientes")
            }
        }
    }
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Limpiar recientes") },
            text = { Text("¿Deseas limpiar el historial de archivos recientes?") },
            confirmButton = {
                TextButton(onClick = { onClearRecent(); showClearDialog = false }) { Text("Limpiar") }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun ThemeChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(selected = selected, onClick = onClick, label = { Text(label) })
}
