package ovh.gabrielhuav.gestor

/** Interfaz `expect` para operaciones de archivo; cada plataforma da su `actual`. */
expect class FileRepository() {
    /** Lista el contenido del directorio interno de la app. */
    fun listInternalFiles(subPath: String = ""): List<FileItem>

    /** Lee el contenido de un archivo dado su [path]. */
    fun readFile(path: String): FileReadResult

    /** Devuelve los metadatos de un archivo o directorio. */
    fun getMetadata(path: String): FileMetadata?

    /** Copia un archivo a [destPath]. */
    fun copyFile(sourcePath: String, destPath: String): Boolean

    /** Mueve un archivo a [destPath]. */
    fun moveFile(sourcePath: String, destPath: String): Boolean

    /** Renombra un archivo. */
    fun renameFile(path: String, newName: String): Boolean

    /** Elimina un archivo. Devuelve true si tuvo éxito. */
    fun deleteFile(path: String): Boolean

    /** Crea un directorio en el almacenamiento interno. */
    fun createDirectory(path: String): Boolean
}
