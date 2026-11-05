package ovh.gabrielhuav.juego

import android.content.Context
import java.io.File

object AndroidContext {
    lateinit var appContext: Context
}

actual class SaveManager {

    private val savesDir: File
        get() = File(AndroidContext.appContext.filesDir, "saves").also { it.mkdirs() }

    actual fun listSaves(): List<SaveInfo> {
        return savesDir.listFiles()?.mapNotNull { file ->
            parseSaveInfo(file)
        }?.sortedByDescending { it.savedAt } ?: emptyList()
    }

    actual fun saveGame(state: GameState, format: SaveFormat, tag: String): String {
        val ts = System.currentTimeMillis()
        val saveId = "partida_$ts"
        val ext = format.name.lowercase()
        val file = File(savesDir, "$saveId.$ext")
        val content = when (format) {
            SaveFormat.TXT -> GameSerializer.toTxt(state.copy(savedAt = ts, tag = tag, saveId = saveId))
            SaveFormat.JSON -> GameSerializer.toJson(state.copy(savedAt = ts, tag = tag, saveId = saveId))
            SaveFormat.XML -> GameSerializer.toXml(state.copy(savedAt = ts, tag = tag, saveId = saveId))
        }
        file.writeText(content, Charsets.UTF_8)
        return saveId
    }

    actual fun loadGame(saveId: String): GameState? {
        val file = savesDir.listFiles()?.firstOrNull { it.nameWithoutExtension == saveId } ?: return null
        val content = file.readText(Charsets.UTF_8)
        val format = when (file.extension.uppercase()) {
            "JSON" -> SaveFormat.JSON
            "XML" -> SaveFormat.XML
            else -> SaveFormat.TXT
        }
        return when (format) {
            SaveFormat.JSON -> GameSerializer.fromJson(content)
            SaveFormat.XML -> GameSerializer.fromXml(content)
            SaveFormat.TXT -> GameSerializer.fromTxt(content)
        }
    }

    actual fun deleteSave(saveId: String): Boolean {
        val file = savesDir.listFiles()?.firstOrNull { it.nameWithoutExtension == saveId } ?: return false
        return file.delete()
    }

    actual fun getSaveContent(saveId: String): String {
        val file = savesDir.listFiles()?.firstOrNull { it.nameWithoutExtension == saveId } ?: return "Archivo no encontrado"
        return try { file.readText(Charsets.UTF_8) } catch (e: Exception) { "Error: ${e.message}" }
    }

    actual fun exportSave(saveId: String): String = getSaveContent(saveId)

    private fun parseSaveInfo(file: File): SaveInfo? {
        return try {
            val content = file.readText(Charsets.UTF_8)
            val format = when (file.extension.uppercase()) {
                "JSON" -> SaveFormat.JSON
                "XML" -> SaveFormat.XML
                else -> SaveFormat.TXT
            }
            val state = when (format) {
                SaveFormat.JSON -> GameSerializer.fromJson(content)
                SaveFormat.XML -> GameSerializer.fromXml(content)
                SaveFormat.TXT -> GameSerializer.fromTxt(content)
            } ?: return null
            SaveInfo(
                saveId = file.nameWithoutExtension,
                format = format,
                score = state.score,
                level = state.level,
                elapsedSeconds = state.elapsedSeconds,
                savedAt = state.savedAt,
                tag = state.tag
            )
        } catch (e: Exception) { null }
    }
}
