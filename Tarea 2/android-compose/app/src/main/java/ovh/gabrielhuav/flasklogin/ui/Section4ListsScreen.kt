package ovh.gabrielhuav.flasklogin.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Section4ListsScreen() {
    var tabSeleccionado by remember { mutableStateOf(0) }
    var itemSeleccionadoParaModal by remember { mutableStateOf<ItemCatalogo?>(null) }
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val elementosFiltrados = remember(AppGlobalState.categoriaFiltro, AppGlobalState.elementos.size) {
        if (AppGlobalState.categoriaFiltro == "Todos") {
            AppGlobalState.elementos.toList()
        } else {
            AppGlobalState.elementos.filter { it.categoria == AppGlobalState.categoriaFiltro }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tabSeleccionado) {
            Tab(
                selected = tabSeleccionado == 0,
                onClick = { tabSeleccionado = 0 },
                text = { Text("Lista Vertical") },
                icon = { Icon(Icons.Default.ViewList, contentDescription = null) }
            )
            Tab(
                selected = tabSeleccionado == 1,
                onClick = { tabSeleccionado = 1 },
                text = { Text("Cuadrícula (Grid)") },
                icon = { Icon(Icons.Default.GridView, contentDescription = null) }
            )
        }

        // Filtro y resumen superior
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Filtro: ${AppGlobalState.categoriaFiltro} | Total: ${elementosFiltrados.size}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { AppGlobalState.recargar() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Recargar lista")
                }
            }
        }

        if (elementosFiltrados.isEmpty()) {
            // Estado vacío
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Inbox, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No hay elementos disponibles", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Cambia el filtro en la Sección 3 o recarga la lista.", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { AppGlobalState.recargar() }) {
                        Text("Recargar elementos")
                    }
                }
            }
        } else {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    scope.launch {
                        isRefreshing = true
                        delay(1000)
                        AppGlobalState.recargar()
                        isRefreshing = false
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) {
                if (tabSeleccionado == 0) {
                    // Vista 1: LazyColumn con SwipeToDismissBox
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = elementosFiltrados,
                            key = { it.id }
                        ) { item ->
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { dismissValue ->
                                    if (dismissValue != SwipeToDismissBoxValue.Settled) {
                                        AppGlobalState.eliminar(item.id)
                                        true
                                    } else {
                                        false
                                    }
                                }
                            )

                            SwipeToDismissBox(
                                state = dismissState,
                                enableDismissFromStartToEnd = false,
                                backgroundContent = {
                                    Surface(
                                        color = MaterialTheme.colorScheme.error,
                                        shape = MaterialTheme.shapes.medium,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(end = 16.dp),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.onError)
                                        }
                                    }
                                }
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { itemSeleccionadoParaModal = item }
                                ) {
                                    ListItem(
                                        headlineContent = { Text(item.titulo, fontWeight = FontWeight.Bold) },
                                        supportingContent = { Text("${item.categoria} • Desliza para borrar") },
                                        leadingContent = { Icon(item.icono, contentDescription = null) },
                                        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) }
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Vista 2: LazyVerticalGrid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(elementosFiltrados, key = { it.id }) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { itemSeleccionadoParaModal = item }
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(item.icono, contentDescription = null, modifier = Modifier.size(36.dp), tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(item.titulo, fontWeight = FontWeight.Bold, maxLines = 1)
                                    Text(item.categoria, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal BottomSheet de detalle
    itemSeleccionadoParaModal?.let { item ->
        ModalBottomSheet(
            onDismissRequest = { itemSeleccionadoParaModal = null }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(item.icono, contentDescription = null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(item.titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                AssistChip(onClick = {}, label = { Text("Categoría: ${item.categoria}") })
                Spacer(modifier = Modifier.height(8.dp))
                Text(item.descripcion, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { itemSeleccionadoParaModal = null },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}
