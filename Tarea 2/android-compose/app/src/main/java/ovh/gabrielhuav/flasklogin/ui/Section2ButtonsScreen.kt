package ovh.gabrielhuav.flasklogin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Section2ButtonsScreen() {
    var ultimoMensaje by remember { mutableStateOf("Ningún botón presionado aún.") }
    var estaCargando by remember { mutableStateOf(false) }
    var segmentoSeleccionado by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner de respuesta
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = ultimoMensaje,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 1. Botones primarios
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1. Botones: Relleno, Contorno y Texto", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Jerarquía visual para acciones de alta, media y baja prioridad.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { ultimoMensaje = "Pulsaste: Botón Relleno (Filled)" }) {
                        Text("Relleno")
                    }
                    OutlinedButton(onClick = { ultimoMensaje = "Pulsaste: Botón Outlined" }) {
                        Text("Contorno")
                    }
                    TextButton(onClick = { ultimoMensaje = "Pulsaste: TextButton" }) {
                        Text("Texto")
                    }
                }
            }
        }

        // 2. Botones de icono y mixto
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2. Botones de Icono y Mixtos", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Iconos de acción directa y botones con icono descriptivo.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FilledIconButton(onClick = { ultimoMensaje = "Pulsaste: Favorito (FilledIconButton)" }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favorito")
                    }
                    OutlinedIconButton(onClick = { ultimoMensaje = "Pulsaste: Compartir (OutlinedIconButton)" }) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir")
                    }
                    Button(onClick = { ultimoMensaje = "Pulsaste: Botón Mixto con Icono" }) {
                        Icon(Icons.Default.Download, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Descargar")
                    }
                }
            }
        }

        // 3. Botones segmentados
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3. Botón Segmentado (SegmentedButton)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Alternancia de opciones mutuamente excluyentes.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    val opciones = listOf("Día", "Semana", "Mes")
                    opciones.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = opciones.size),
                            onClick = {
                                segmentoSeleccionado = index
                                ultimoMensaje = "Segmento elegido: $label"
                            },
                            selected = index == segmentoSeleccionado
                        ) {
                            Text(label)
                        }
                    }
                }
            }
        }

        // 4. Botón deshabilitado y en estado de carga
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("4. Botón Deshabilitado y en Estado de Carga", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Comportamiento cuando una acción no está disponible o está en proceso asíncrono.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {},
                        enabled = false
                    ) {
                        Text("Deshabilitado")
                    }
                    Button(
                        onClick = {
                            if (!estaCargando) {
                                estaCargando = true
                                ultimoMensaje = "Cargando solicitud..."
                                scope.launch {
                                    delay(3000)
                                    estaCargando = false
                                    ultimoMensaje = "¡Carga completada en Compose!"
                                }
                            }
                        },
                        enabled = !estaCargando
                    ) {
                        if (estaCargando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cargando...")
                        } else {
                            Text("Simular Carga (3s)")
                        }
                    }
                }
            }
        }

        // 5. Botón de Acción Flotante (FAB normal y extendido)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("5. Botón de Acción Flotante (FAB)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Representa la acción principal. Muestra versión estándar circular y versión extendida.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FloatingActionButton(
                        onClick = { ultimoMensaje = "Pulsaste: FAB Normal circular" }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar")
                    }

                    ExtendedFloatingActionButton(
                        onClick = { ultimoMensaje = "Pulsaste: Extended FAB" },
                        icon = { Icon(Icons.Default.Navigation, contentDescription = null) },
                        text = { Text("FAB Extendido") }
                    )
                }
            }
        }
    }
}
