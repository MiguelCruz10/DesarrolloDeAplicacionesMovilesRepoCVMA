package ovh.gabrielhuav.flasklogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ovh.gabrielhuav.flasklogin.ui.AuthScreen
import ovh.gabrielhuav.flasklogin.ui.TaskScreen
import ovh.gabrielhuav.flasklogin.ui.theme.FlaskLoginTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlaskLoginTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    // Variable para guardar el ID del usuario cuando inicie sesión
    var currentUserId by remember { mutableStateOf<Int?>(null) }

    // Si no hay usuario, mostramos el Login/Registro
    if (currentUserId == null) {
        AuthScreen(onLoginSuccess = { userId ->
            currentUserId = userId
        })
    } else {
        // Si ya hay usuario, mostramos sus Tareas
        TaskScreen(
            userId = currentUserId!!,
            onLogout = { currentUserId = null } // Al salir, borramos el ID
        )
    }
}