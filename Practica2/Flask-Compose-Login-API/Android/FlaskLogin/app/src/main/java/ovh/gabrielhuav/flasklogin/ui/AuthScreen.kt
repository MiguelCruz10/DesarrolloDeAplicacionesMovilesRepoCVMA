package ovh.gabrielhuav.flasklogin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ovh.gabrielhuav.flasklogin.network.ApiClient
import ovh.gabrielhuav.flasklogin.network.ApiService
import ovh.gabrielhuav.flasklogin.network.LoginRequest
import ovh.gabrielhuav.flasklogin.network.RegisterRequest

@Composable
fun AuthScreen(onLoginSuccess: (Int) -> Unit) {
    var isLoginMode by remember { mutableStateOf(true) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val apiService = ApiClient.retrofit.create(ApiService::class.java)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isLoginMode) "Inicio de Sesión" else "Registro de Usuario",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                message = "Cargando..."
                coroutineScope.launch {
                    try {
                        if (isLoginMode) {
                            val response = apiService.login(LoginRequest(username, password))
                            if (response.isSuccessful && response.body()?.status == "success") {
                                message = "Login exitoso"
                                response.body()?.user_id?.let { onLoginSuccess(it) }
                            } else {
                                message = "Error: Credenciales incorrectas"
                            }
                        } else {
                            val response = apiService.register(RegisterRequest(username, password))
                            if (response.isSuccessful) {
                                message = "Registro exitoso. Ahora puedes iniciar sesión."
                                isLoginMode = true
                            } else {
                                message = "Error: El usuario ya existe o hubo un fallo."
                            }
                        }
                    } catch (e: Exception) {
                        message = "Error de conexión con el servidor"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoginMode) "Entrar" else "Registrarme")
        }

        TextButton(onClick = {
            isLoginMode = !isLoginMode
            message = ""
        }) {
            Text(if (isLoginMode) "¿No tienes cuenta? Regístrate aquí" else "¿Ya tienes cuenta? Inicia sesión")
        }

        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = message, color = MaterialTheme.colorScheme.error)
        }
    }
}
