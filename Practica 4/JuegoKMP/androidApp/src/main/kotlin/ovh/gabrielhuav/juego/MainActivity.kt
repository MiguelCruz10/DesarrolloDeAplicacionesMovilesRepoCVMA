package ovh.gabrielhuav.juego

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // Inicializar singletons de Android
        AndroidContext.appContext = applicationContext
        AndroidPreferences.sharedPreferences =
            getSharedPreferences("juego_prefs", MODE_PRIVATE)
        setContent { App() }
    }
}
