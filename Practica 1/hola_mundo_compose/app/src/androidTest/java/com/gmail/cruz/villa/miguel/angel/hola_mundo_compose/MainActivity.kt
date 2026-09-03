import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.gmail.cruz.villa.miguel.angel.hola_mundo_compose.ui.theme.Hola_mundo_composeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Hola_mundo_composeTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    InformacionEstudiante()
                }
            }
        }
    }
}

@Composable
fun InformacionEstudiante() {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hola Mundo",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Text(text = "Cruz Villa Miguel Ángel", fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp))
        Text(text = "Boleta: 2024630153", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
        Text(text = "Grupo: 7CV4", fontSize = 18.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun InformacionEstudiantePreview() {
    Hola_mundo_composeTheme {
        InformacionEstudiante()
    }
}