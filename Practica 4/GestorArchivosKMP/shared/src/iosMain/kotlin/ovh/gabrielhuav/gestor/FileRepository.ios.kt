package ovh.gabrielhuav.gestor

import platform.Foundation.*

actual class FileRepository {

    private val internalDir: String
        get() = NSFileManager.defaultManager.URLsForDirectory(
            NSDocumentDirectory, NSUserDomainMask
        ).firstOrNull()?.path ?: ""

    private fun nsFileManager() = NSFileManager.defaultManager

    actual fun listInternalFiles(subPath: String): List<FileItem> {
        val dirPath = if (subPath.isEmpty()) internalDir else subPath
        val fm = nsFileManager()
        val contents = fm.contentsOfDirectoryAtPath(dirPath, error = null) ?: return emptyList()
        return (contents as List<*>).mapNotNull { it as? String }.map { name ->
            val fullPath = "$dirPath/$name"
            val attrs = fm.attributesOfItemAtPath(fullPath, error = null)
            val isDir = (attrs?.get(NSFileType) as? String) == NSFileTypeDirectory
            val size = (attrs?.get(NSFileSize) as? NSNumber)?.longLongValue ?: 0L
            val modified = ((attrs?.get(NSFileModificationDate) as? NSDate)
                ?.timeIntervalSince1970 ?: 0.0).toLong() * 1000L
            val ext = name.substringAfterLast('.', "")
            FileItem(
                name = name,
                path = fullPath,
                isDirectory = isDir,
                size = if (isDir) -1L else size,
                lastModified = modified,
                mimeType = if (isDir) "" else getMimeType(ext)
            )
        }.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))
    }

    actual fun readFile(path: String): FileReadResult {
        return try {
            val fm = nsFileManager()
            if (!fm.fileExistsAtPath(path)) return FileReadResult.Error("El archivo no existe")
            val attrs = fm.attributesOfItemAtPath(path, error = null)
            val size = (attrs?.get(NSFileSize) as? NSNumber)?.longLongValue ?: 0L
            if (size > 10 * 1024 * 1024) return FileReadResult.Error("Archivo demasiado grande")

            val ext = path.substringAfterLast('.', "")
            val mime = getMimeType(ext)
            val data = NSData.dataWithContentsOfFile(path) ?: return FileReadResult.Error("No se pudo leer")

            when {
                mime.startsWith("text/") || mime.contains("json") || mime.contains("xml") -> {
                    val text = NSString.create(data, NSUTF8StringEncoding)?.toString()
                        ?: return FileReadResult.Error("No se pudo decodificar como UTF-8")
                    FileReadResult.TextContent(text)
                }
                mime.startsWith("image/") -> {
                    val bytes = data.bytes?.readBytes(data.length.toInt()) ?: ByteArray(0)
                    FileReadResult.ImageBytes(bytes)
                }
                else -> FileReadResult.UnsupportedFormat(mime)
            }
        } catch (e: Exception) {
            FileReadResult.Error(e.message ?: "Error desconocido")
        }
    }

    actual fun getMetadata(path: String): FileMetadata? {
        return try {
            val fm = nsFileManager()
            if (!fm.fileExistsAtPath(path)) return null
            val attrs = fm.attributesOfItemAtPath(path, error = null)
            val isDir = (attrs?.get(NSFileType) as? String) == NSFileTypeDirectory
            val size = (attrs?.get(NSFileSize) as? NSNumber)?.longLongValue ?: 0L
            val modified = ((attrs?.get(NSFileModificationDate) as? NSDate)
                ?.timeIntervalSince1970 ?: 0.0).toLong() * 1000L
            val name = path.substringAfterLast('/')
            val ext = name.substringAfterLast('.', "")
            FileMetadata(
                name = name,
                path = path,
                size = if (isDir) -1L else size,
                lastModified = modified,
                mimeType = getMimeType(ext),
                isDirectory = isDir
            )
        } catch (e: Exception) { null }
    }

    actual fun copyFile(sourcePath: String, destPath: String): Boolean {
        return try {
            nsFileManager().copyItemAtPath(sourcePath, toPath = destPath, error = null)
        } catch (e: Exception) { false }
    }

    actual fun moveFile(sourcePath: String, destPath: String): Boolean {
        return try {
            nsFileManager().moveItemAtPath(sourcePath, toPath = destPath, error = null)
        } catch (e: Exception) { false }
    }

    actual fun renameFile(path: String, newName: String): Boolean {
        val dir = path.substringBeforeLast('/')
        return moveFile(path, "$dir/$newName")
    }

    actual fun deleteFile(path: String): Boolean {
        return try {
            nsFileManager().removeItemAtPath(path, error = null)
        } catch (e: Exception) { false }
    }

    actual fun createDirectory(path: String): Boolean {
        return try {
            nsFileManager().createDirectoryAtPath(path,
                withIntermediateDirectories = true,
                attributes = null,
                error = null)
        } catch (e: Exception) { false }
    }

    private fun getMimeType(ext: String): String = when (ext.lowercase()) {
        "txt", "md", "log" -> "text/plain"
        "json" -> "application/json"
        "xml" -> "text/xml"
        "kt", "java" -> "text/plain"
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        "webp" -> "image/webp"
        "pdf" -> "application/pdf"
        else -> "application/octet-stream"
    }
}
