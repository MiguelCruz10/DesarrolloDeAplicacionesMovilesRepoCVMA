package ovh.gabrielhuav.flasklogin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ovh.gabrielhuav.flasklogin.network.ApiClient
import ovh.gabrielhuav.flasklogin.network.ApiService
import ovh.gabrielhuav.flasklogin.network.TaskRequest
import ovh.gabrielhuav.flasklogin.network.TaskResponse
import ovh.gabrielhuav.flasklogin.network.TaskUpdateRequest

@Composable
fun TaskScreen(userId: Int, onLogout: () -> Unit) {
    var tasks by remember { mutableStateOf<List<TaskResponse>>(emptyList()) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val apiService = ApiClient.retrofit.create(ApiService::class.java)

    // Función para recargar la lista de tareas
    fun loadTasks() {
        coroutineScope.launch {
            try {
                val response = apiService.getTasks(userId)
                if (response.isSuccessful) {
                    tasks = response.body() ?: emptyList()
                }
            } catch (e: Exception) { }
        }
    }

    // Cargar las tareas al entrar a la pantalla
    LaunchedEffect(Unit) {
        loadTasks()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mis Tareas", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = onLogout) { Text("Salir") }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título de la tarea") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (title.isNotEmpty()) {
                    coroutineScope.launch {
                        apiService.createTask(TaskRequest(title, description, userId))
                        title = ""
                        description = ""
                        loadTasks() // Recargamos la lista
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Crear Tarea")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks) { task ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(task.title, style = MaterialTheme.typography.titleMedium)
                        Text(task.description, style = MaterialTheme.typography.bodyMedium)

                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = {
                                coroutineScope.launch {
                                    // Para la práctica, simplemente le agregaremos "(Editado)" al título
                                    apiService.updateTask(task.id, TaskUpdateRequest("${task.title} (Editado)", task.description))
                                    loadTasks()
                                }
                            }) { Text("Actualizar") }

                            TextButton(onClick = {
                                coroutineScope.launch {
                                    apiService.deleteTask(task.id)
                                    loadTasks()
                                }
                            }) { Text("Borrar") }
                        }
                    }
                }
            }
        }
    }
}