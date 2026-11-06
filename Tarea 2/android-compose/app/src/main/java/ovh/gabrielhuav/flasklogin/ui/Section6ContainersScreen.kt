package ovh.gabrielhuav.flasklogin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Section6ContainersScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Fila y Columna
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1. Distribución en Fila (Row) y Columna (Column)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Organización lineal en el eje horizontal y vertical.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text("Caja A", modifier = Modifier.padding(12.dp))
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text("Caja B", modifier = Modifier.padding(12.dp))
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text("Caja C", modifier = Modifier.padding(12.dp))
                    }
                }
            }
        }

        // 2. Vista superpuesta (Box)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2. Vista Superpuesta (Box / Stack)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Permite apilar composables unos sobre otros en el eje Z.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Capa Base de Fondo (Box)",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Layers, contentDescription = null, tint = Color.white, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Superpuesto", color = Color.white, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        // 3. Distribución con pesos proporcionales (weight)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3. Distribución Proporcional con Pesos (Modifier.weight)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Reparto exacto del espacio mediante pesos: 1x, 2x y 1x (total 4 partes).", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .background(Color(0xFF4DB6AC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Weight 1 (25%)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .height(48.dp)
                            .background(Color(0xFF00897B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Weight 2 (50%)", color = Color.white, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .background(Color(0xFF4DB6AC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Weight 1 (25%)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        // 4. Scroll vertical
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("4. Contenedor Desplazable (Modifier.verticalScroll)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("El contenedor general utiliza Modifier.verticalScroll(rememberScrollState()) evitando problemas de desbordamiento en pantallas pequeñas.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}
