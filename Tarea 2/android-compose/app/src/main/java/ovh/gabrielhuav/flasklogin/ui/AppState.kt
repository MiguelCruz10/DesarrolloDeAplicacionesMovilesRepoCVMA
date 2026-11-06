package ovh.gabrielhuav.flasklogin.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector

data class ItemCatalogo(
    val id: Long,
    val titulo: String,
    val categoria: String,
    val descripcion: String,
    val icono: ImageVector
)

object AppGlobalState {
    var nombreUsuario by mutableStateOf("Estudiante")
    var categoriaFiltro by mutableStateOf("Todos")

    val elementos = mutableStateListOf<ItemCatalogo>()

    init {
        recargar()
    }

    fun recargar() {
        elementos.clear()
        elementos.addAll(
            listOf(
                ItemCatalogo(1, "Cámara Réflex", "Fotografía", "Sensor Full Frame para alta resolución.", Icons.Default.CameraAlt),
                ItemCatalogo(2, "Lente 50mm f/1.8", "Fotografía", "Lente fijo para retratos y bokeh.", Icons.Default.Camera),
                ItemCatalogo(3, "Trípode de Aluminio", "Fotografía", "Estabilidad en tomas de larga exposición.", Icons.Default.Landscape),
                ItemCatalogo(4, "Laptop Gamer Pro", "Cómputo", "Procesador de 16 núcleos y 32GB RAM.", Icons.Default.Laptop),
                ItemCatalogo(5, "Teclado Mecánico", "Cómputo", "Switches táctiles e iluminación RGB.", Icons.Default.Keyboard),
                ItemCatalogo(6, "Ratón Ergonómico", "Cómputo", "Sensor óptico de alta precisión.", Icons.Default.Mouse),
                ItemCatalogo(7, "Monitor 4K UltraWide", "Cómputo", "Panel IPS de 144Hz para diseño.", Icons.Default.DesktopWindows),
                ItemCatalogo(8, "Audífonos Bluetooth", "Audio", "Cancelación activa de ruido.", Icons.Default.Headphones),
                ItemCatalogo(9, "Micrófono Condensador", "Audio", "Patrón cardioide para streaming.", Icons.Default.Mic),
                ItemCatalogo(10, "Bocina Portátil", "Audio", "Resistente al agua con graves profundos.", Icons.Default.Speaker),
                ItemCatalogo(11, "Smartphone 5G", "Móviles", "Pantalla OLED 120Hz.", Icons.Default.PhoneAndroid),
                ItemCatalogo(12, "Smartwatch Deportivo", "Móviles", "GPS y pulsómetro continuo.", Icons.Default.Watch),
                ItemCatalogo(13, "Tablet Digitalizadora", "Móviles", "Lápiz con 4096 niveles de presión.", Icons.Default.Tablet),
                ItemCatalogo(14, "Consola Portátil", "Videojuegos", "Pantalla táctil y mandos desmontables.", Icons.Default.Gamepad),
                ItemCatalogo(15, "Control Inalámbrico", "Videojuegos", "Gatillos adaptativos y respuesta háptica.", Icons.Default.SportsEsports),
                ItemCatalogo(16, "Disco SSD NVMe 1TB", "Cómputo", "Lectura a más de 7000 MB/s.", Icons.Default.Storage)
            )
        )
    }

    fun eliminar(id: Long) {
        elementos.removeAll { it.id == id }
    }

    fun agregar(titulo: String, categoria: String) {
        elementos.add(
            0,
            ItemCatalogo(
                id = System.currentTimeMillis(),
                titulo = titulo,
                categoria = categoria,
                descripcion = "Elemento añadido dinámicamente desde Compose.",
                icono = Icons.Default.Star
            )
        )
    }
}
