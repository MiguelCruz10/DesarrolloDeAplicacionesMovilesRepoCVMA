package ovh.gabrielhuav.juego

import android.os.Build
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual fun plataforma(): String = "Android ${Build.VERSION.RELEASE}"
actual fun currentTimeMillis(): Long = System.currentTimeMillis()
actual fun formatEpoch(epochMillis: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(epochMillis))
}
