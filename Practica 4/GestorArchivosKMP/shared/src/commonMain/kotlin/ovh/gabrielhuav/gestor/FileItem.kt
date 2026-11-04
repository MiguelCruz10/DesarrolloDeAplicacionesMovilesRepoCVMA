package ovh.gabrielhuav.gestor

/** Representa un archivo o directorio en el explorador. */
data class FileItem(
    val name: String,
    val path: String,         // Ruta lógica (URI string en Android, path en iOS)
    val isDirectory: Boolean,
    val size: Long = 0L,      // En bytes; -1 si no disponible
    val lastModified: Long = 0L, // Epoch millis; 0 si no disponible
    val mimeType: String = "", // "" si no disponible / es directorio
)

/** Metadatos extendidos para mostrar en el panel de detalles. */
data class FileMetadata(
    val name: String,
    val path: String,
    val size: Long,
    val lastModified: Long,
    val mimeType: String,
    val isDirectory: Boolean,
)

/** Resultado de leer el contenido de un archivo. */
sealed class FileReadResult {
    data class TextContent(val text: String) : FileReadResult()
    data class ImageBytes(val bytes: ByteArray) : FileReadResult()
    data class UnsupportedFormat(val mimeType: String) : FileReadResult()
    data class Error(val message: String) : FileReadResult()
}
