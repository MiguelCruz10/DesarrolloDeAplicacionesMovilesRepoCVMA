package ovh.gabrielhuav.flasklogin.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Section3SelectionScreen() {
    // Checkboxes
    var chkNotificaciones by remember { mutableStateOf(true) }
    var chkTerminos by remember { mutableStateOf(false) }
    var triState by remember { mutableStateOf(ToggleableState.Indeterminate) }

    // Radio
    var radioSeleccionado by remember { mutableStateOf(1) }

    // Switch
    var switchSincronizacion by remember { mutableStateOf(true) }

    // Sliders
    var sliderSimple by remember { mutableStateOf(45f) }
    var sliderRango by remember { mutableStateOf(20f..75f) }

    // Dropdown
    val categorias = listOf("Todos", "Fotografía", "Cómputo", "Audio", "Móviles", "Videojuegos")
    var dropdownExpanded by remember { mutableStateOf(false) }

    // Pickers
    var showDatePicker by remember { mutableStateOf(false) }
    var fechaSeleccionadaTexto by remember { mutableStateOf("Elegir Fecha") }
    val datePickerState = rememberDatePickerState()

    var showTimePicker by remember { mutableStateOf(false) }
    var horaSeleccionadaTexto by remember { mutableStateOf("Elegir Hora") }
    val timePickerState = rememberTimePickerState()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Checkboxes con indeterminado
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1. Casillas de Verificación (Checkboxes)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Opciones independientes incluyendo caso TriState (indeterminado).", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = chkNotificaciones, onCheckedChange = { chkNotificaciones = it })
                    Text("Recibir notificaciones por correo")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = chkTerminos, onCheckedChange = { chkTerminos = it })
                    Text("Aceptar términos y condiciones")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TriStateCheckbox(
                        state = triState,
                        onClick = {
                            triState = when (triState) {
                                ToggleableState.On -> ToggleableState.Off
                                ToggleableState.Off -> ToggleableState.Indeterminate
                                ToggleableState.Indeterminate -> ToggleableState.On
                            }
                        }
                    )
                    Text("Estado Tri-estado: ${triState.name}")
                }
            }
        }

        // 2. Radio buttons excluyentes
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2. Grupo de Selección Excluyente (RadioButtons)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Permite únicamente una opción activa dentro del grupo.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(8.dp))

                val opcionesRadio = listOf(1 to "Acceso Estándar (Lectura)", 2 to "Acceso Moderador (Edición)", 3 to "Acceso Administrador (Total)")
                opcionesRadio.forEach { (id, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { radioSeleccionado = id },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (radioSeleccionado == id),
                            onClick = { radioSeleccionado = id }
                        )
                        Text(label)
                    }
                }
            }
        }

        // 3. Switch
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3. Interruptor (Switch)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Control directo binario para ajustes inmediatos.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Sincronización en segundo plano", fontWeight = FontWeight.SemiBold)
                        Text(if (switchSincronizacion) "Estado: Activo" else "Estado: Inactivo", style = MaterialTheme.typography.bodySmall)
                    }
                    Switch(
                        checked = switchSincronizacion,
                        onCheckedChange = { switchSincronizacion = it }
                    )
                }
            }
        }

        // 4. Sliders (Simple y Rango)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("4. Sliders (Único y de Rango)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Selección numérica continua e intervalos acotados.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))

                Text("Volumen / Nivel: ${sliderSimple.toInt()}%")
                Slider(
                    value = sliderSimple,
                    onValueChange = { sliderSimple = it },
                    valueRange = 0f..100f
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text("Rango de Precios: $${sliderRango.start.toInt()} - $${sliderRango.endInclusive.toInt()}")
                RangeSlider(
                    value = sliderRango,
                    onValueChange = { sliderRango = it },
                    valueRange = 0f..100f
                )
            }
        }

        // 5. Menú desplegable con vinculación a Sección 4
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("5. Menú Desplegable (Dropdown)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Filtra en vivo la lista de la Sección 4 (vínculo transversal).", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = AppGlobalState.categoriaFiltro,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Filtrar categoría en catálogo") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        categorias.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    AppGlobalState.categoriaFiltro = cat
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // 6. DatePicker y TimePicker modales
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("6. Selectores de Fecha y Hora", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Diálogos estándar para selección temporal.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(fechaSeleccionadaTexto)
                    }
                    OutlinedButton(onClick = { showTimePicker = true }) {
                        Icon(Icons.Default.Schedule, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(horaSeleccionadaTexto)
                    }
                }
            }
        }
    }

    // Modal DatePickerDialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Date(millis)
                        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        fechaSeleccionadaTexto = format.format(date)
                    }
                    showDatePicker = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Modal TimePickerDialog sencillo
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    horaSeleccionadaTexto = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                    showTimePicker = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancelar")
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}
