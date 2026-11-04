package ovh.gabrielhuav.gestor

import android.content.Context
import android.webkit.MimeTypeMap
import java.io.File

/** El Context se inyecta a través del singleton AndroidContext al inicializar la app. */
object AndroidContext {
    lateinit var appContext: Context
}

actual class FileRepository {

    private val internalDir: File get() = AndroidContext.appContext.filesDir

    actual fun listInternalFiles(subPath: String): List<FileItem> {
        val dir = if (subPath.isEmpty()) internalDir else File(subPath)
        if (!dir.exists() || !dir.isDirectory) return emptyList()
        return dir.listFiles()?.map { file ->
            FileItem(
                name = file.name,
                path = file.absolutePath,
                isDirectory = file.isDirectory,
                size = if (file.isDirectory) -1L else file.length(),
                lastModified = file.lastModified(),
                mimeType = if (file.isDirectory) "" else getMimeType(file.extension)
            )
        }?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))
            ?: emptyList()
    }

    actual fun readFile(path: String): FileReadResult {
        return try {
            val file = File(path)
            if (!file.exists()) return FileReadResult.Error("El archivo no existe")
            if (file.length() > 10 * 1024 * 1024) return FileReadResult.Error("Archivo demasiado grande (>10 MB)")

            val mime = getMimeType(file.extension)
            when {
                mime.startsWith("text/") || mime.contains("json") || mime.contains("xml") ->
                    FileReadResult.TextContent(file.readText(Charsets.UTF_8))
                mime.startsWith("image/") ->
                    FileReadResult.ImageBytes(file.readBytes())
                else -> FileReadResult.UnsupportedFormat(mime)
            }
        } catch (e: Exception) {
            FileReadResult.Error(e.message ?: "Error desconocido")
        }
    }

    actual fun getMetadata(path: String): FileMetadata? {
        return try {
            val file = File(path)
            if (!file.exists()) return null
            FileMetadata(
                name = file.name,
                path = file.absolutePath,
                size = if (file.isDirectory) -1L else file.length(),
                lastModified = file.lastModified(),
                mimeType = getMimeType(file.extension),
                isDirectory = file.isDirectory
            )
        } catch (e: Exception) { null }
    }

    actual fun copyFile(sourcePath: String, destPath: String): Boolean {
        return try {
            File(sourcePath).copyTo(File(destPath), overwrite = false)
            true
        } catch (e: Exception) { false }
    }

    actual fun moveFile(sourcePath: String, destPath: String): Boolean {
        return try {
            val src = File(sourcePath)
            val dst = File(destPath)
            src.copyTo(dst, overwrite = false)
            src.delete()
            true
        } catch (e: Exception) { false }
    }

    actual fun renameFile(path: String, newName: String): Boolean {
        return try {
            val file = File(path)
            val newFile = File(file.parent, newName)
            file.renameTo(newFile)
        } catch (e: Exception) { false }
    }

    actual fun deleteFile(path: String): Boolean {
        return try {
            val file = File(path)
            if (file.isDirectory) file.deleteRecursively() else file.delete()
        } catch (e: Exception) { false }
    }

    actual fun createDirectory(path: String): Boolean {
        return try {
            File(path).mkdirs()
        } catch (e: Exception) { false }
    }

    private fun getMimeType(extension: String): String {
        if (extension.isEmpty()) return "application/octet-stream"
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
            ?: when (extension.lowercase()) {
                "txt", "md", "log" -> "text/plain"
                "json" -> "application/json"
                "xml" -> "text/xml"
                "kt", "java" -> "text/plain"
                "jpg", "jpeg" -> "image/jpeg"
                "png" -> "image/png"
                "gif" -> "image/gif"
                "webp" -> "image/webp"
                else -> "application/octet-stream"
            }
    }
}
