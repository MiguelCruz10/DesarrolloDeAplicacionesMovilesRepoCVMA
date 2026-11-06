package ovh.gabrielhuav.flasklogin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Section5FeedbackScreen(
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var notificacionesBadge by remember { mutableStateOf(3) }
    val progresoDeterminado = 0.65f

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Tipografías
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1. Tipografías y Jerarquía de Texto", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Escalas de Material 3 para títulos, subtítulos y énfasis.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))

                Text("Headline Small", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Usuario activo: ${AppGlobalState.nombreUsuario}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Text("Cuerpo de texto estándar (Body Medium). Demuestra legibilidad, espaciado y contraste dinámico.", style = MaterialTheme.typography.bodyMedium)
            }
        }

        // 2. Modos de escalado e imágenes
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2. Carga y Escalado de Imágenes / Formas", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Contenedores recortados simulando ContentScale.Crop y ContentScale.Fit.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(90.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Image, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Text("Scale: Crop", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(90.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.FitScreen, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                            Text("Scale: Fit", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        // 3. Barras de progreso
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3. Indicadores de Progreso", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Lineal determinado y circular indeterminado / determinado.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { progresoDeterminado },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Progreso: ${(progresoDeterminado * 100).toInt()}%", style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Indeterminado", style = MaterialTheme.typography.labelSmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(progress = { 0.8f })
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Determinado 80%", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        // 4. Diálogos, Snackbar y BottomSheet
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("4. Retroalimentación: Snackbar, Diálogo y Hoja", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Muestra retroalimentación visual contextual e interactiva.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                val resultado = snackbarHostState.showSnackbar(
                                    message = "Cambios guardados en Compose",
                                    actionLabel = "Deshacer"
                                )
                                if (resultado == SnackbarResult.ActionPerformed) {
                                    // Acción al deshacer
                                }
                            }
                        }
                    ) {
                        Text("Snackbar")
                    }

                    OutlinedButton(onClick = { showDialog = true }) {
                        Text("Diálogo")
                    }

                    FilledTonalButton(onClick = { showBottomSheet = true }) {
                        Text("Modal Sheet")
                    }
                }
            }
        }

        // 5. Badges y tarjetas
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("5. Tarjeta con Badges (Insignias)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Notificaciones pendientes mediante BadgedBox interactivo.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    BadgedBox(
                        badge = {
                            Badge { Text("$notificacionesBadge") }
                        }
                    ) {
                        FilledTonalIconButton(onClick = { notificacionesBadge++ }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                        }
                    }
                    Text("Toca la campana para aumentar las notificaciones")
                }
            }
        }
    }

    // Cuadro de Diálogo AlertDialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar Operación") },
            text = { Text("¿Deseas confirmar la acción para el usuario '${AppGlobalState.nombreUsuario}'?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    scope.launch {
                        snackbarHostState.showSnackbar("Operación confirmada")
                    }
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Modal Bottom Sheet
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text("Hoja Inferior Modal (Compose)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Usuario vinculado: ${AppGlobalState.nombreUsuario}")
                Text("Esta hoja deslizable permite desplegar acciones adicionales sin cambiar de pantalla.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showBottomSheet = false },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Entendido")
                }
            }
        }
    }
}
