package ovh.gabrielhuav.flasklogin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Section1TextScreen() {
    var textSimple by remember { mutableStateOf("") }
    var textValidacion by remember { mutableStateOf("") }
    var isErrorValidacion by remember { mutableStateOf(false) }

    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var textNumero by remember { mutableStateOf("") }
    var textTelefono by remember { mutableStateOf("") }
    var textMultiline by remember { mutableStateOf("") }

    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }

    // Autocomplete / Exposed Dropdown
    val tecnologias = listOf("Android Views", "Jetpack Compose", "Flutter", "Kotlin", "Dart", "Material Design")
    var expandedDropdown by remember { mutableStateOf(false) }
    var selectedTech by remember { mutableStateOf(tecnologias[0]) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Campo de texto simple
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1. Campo de texto simple", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Captura texto estándar. Guarda el nombre de usuario globalmente para las demás secciones.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = textSimple,
                    onValueChange = { textSimple = it },
                    label = { Text("Nombre de usuario") },
                    placeholder = { Text("Ej. Miguel Ángel") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (textSimple.isNotBlank()) {
                                AppGlobalState.nombreUsuario = textSimple.trim()
                            }
                        }) {
                            Icon(Icons.Default.Save, contentDescription = "Guardar")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Nombre actual: ${AppGlobalState.nombreUsuario}", style = MaterialTheme.typography.bodySmall)
            }
        }

        // 2. Campo con validación y error visible
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2. Campo con validación y error visible", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Evalúa longitud mínima (al menos 5 caracteres). Muestra estado de error y mensaje de soporte.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = textValidacion,
                    onValueChange = {
                        textValidacion = it
                        isErrorValidacion = it.isNotBlank() && it.length < 5
                    },
                    label = { Text("Correo o Matrícula") },
                    isError = isErrorValidacion,
                    supportingText = {
                        if (isErrorValidacion) {
                            Text("Debe contener al menos 5 caracteres", color = MaterialTheme.colorScheme.error)
                        } else {
                            Text("Mínimo 5 caracteres")
                        }
                    },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    trailingIcon = {
                        if (isErrorValidacion) {
                            Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // 3. Contraseña con toggle
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3. Contraseña con ocultar/mostrar", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Utiliza VisualTransformation para conmutar entre caracteres ocultos y visibles.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Alternar contraseña"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // 4. Teclados numérico y telefónico
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("4. Tipos de teclado", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Ajusta el KeyboardType para abrir el teclado especializado según el dato requerido.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = textNumero,
                    onValueChange = { textNumero = it },
                    label = { Text("Teclado Numérico (Boleta/PIN)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = { Icon(Icons.Default.Pin, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = textTelefono,
                    onValueChange = { textTelefono = it },
                    label = { Text("Teclado Telefónico") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // 5. Campo multilínea
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("5. Campo multilínea", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Permite notas o comentarios con minLines y sin límite horizontal único.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = textMultiline,
                    onValueChange = { textMultiline = it },
                    label = { Text("Comentarios o Descripción") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // 6. Autocompletado / Opciones desplegables
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("6. Sugerencias desplegables (ExposedDropdown)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Ofrece selección entre un catálogo de sugerencias automáticas de tecnologías.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedDropdown,
                    onExpandedChange = { expandedDropdown = !expandedDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedTech,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tecnología favorita") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false }
                    ) {
                        tecnologias.forEach { tech ->
                            DropdownMenuItem(
                                text = { Text(tech) },
                                onClick = {
                                    selectedTech = tech
                                    expandedDropdown = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // 7. Barra de búsqueda
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("7. Barra de búsqueda", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("DockedSearchBar interactiva con borrado y ejecución de filtro de búsqueda.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))
                DockedSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { searchActive = false },
                    active = searchActive,
                    onActiveChange = { searchActive = it },
                    placeholder = { Text("Buscar elemento...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Resultados para: $searchQuery", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}
