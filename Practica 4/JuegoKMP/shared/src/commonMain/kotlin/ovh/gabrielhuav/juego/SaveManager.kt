package ovh.gabrielhuav.juego

/**
 * Interfaz `expect` para el gestor de guardado de partidas.
 * Cada plataforma implementa cómo leer/escribir archivos.
 */
expect class SaveManager() {

    /** Devuelve la lista de partidas guardadas con sus metadatos. */
    fun listSaves(): List<SaveInfo>

    /** Guarda una partida en el formato especificado. Devuelve el ID del guardado. */
    fun saveGame(state: GameState, format: SaveFormat, tag: String): String

    /** Carga una partida a partir de su [saveId]. */
    fun loadGame(saveId: String): GameState?

    /** Elimina una partida por su [saveId]. */
    fun deleteSave(saveId: String): Boolean

    /** Devuelve el contenido textual de un archivo de guardado (para visualización). */
    fun getSaveContent(saveId: String): String

    /** Exporta el contenido de la partida como texto para compartir. */
    fun exportSave(saveId: String): String
}

enum class SaveFormat { TXT, XML, JSON }

data class SaveInfo(
    val saveId: String,
    val format: SaveFormat,
    val score: Long,
    val level: Int,
    val elapsedSeconds: Long,
    val savedAt: Long,
    val tag: String,
)
