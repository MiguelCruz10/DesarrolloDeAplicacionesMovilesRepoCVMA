package ovh.gabrielhuav.juego

import platform.Foundation.*

actual class SaveManager {

    private val savesDir: String
        get() {
            val docs = NSFileManager.defaultManager.URLsForDirectory(
                NSDocumentDirectory, NSUserDomainMask
            ).firstOrNull()?.path ?: ""
            val dir = "$docs/saves"
            NSFileManager.defaultManager.createDirectoryAtPath(
                dir, withIntermediateDirectories = true, attributes = null, error = null)
            return dir
        }

    actual fun listSaves(): List<SaveInfo> {
        val fm = NSFileManager.defaultManager
        val files = fm.contentsOfDirectoryAtPath(savesDir, error = null) as? List<String> ?: return emptyList()
        return files.mapNotNull { name ->
            val path = "$savesDir/$name"
            parseSaveInfo(path, name)
        }.sortedByDescending { it.savedAt }
    }

    actual fun saveGame(state: GameState, format: SaveFormat, tag: String): String {
        val ts = (NSDate().timeIntervalSince1970 * 1000).toLong()
        val saveId = "partida_$ts"
        val ext = format.name.lowercase()
        val path = "$savesDir/$saveId.$ext"
        val content = when (format) {
            SaveFormat.TXT -> GameSerializer.toTxt(state.copy(savedAt = ts, tag = tag, saveId = saveId))
            SaveFormat.JSON -> GameSerializer.toJson(state.copy(savedAt = ts, tag = tag, saveId = saveId))
            SaveFormat.XML -> GameSerializer.toXml(state.copy(savedAt = ts, tag = tag, saveId = saveId))
        }
        (content as NSString).writeToFile(path, atomically = true, encoding = NSUTF8StringEncoding, error = null)
        return saveId
    }

    actual fun loadGame(saveId: String): GameState? {
        val fm = NSFileManager.defaultManager
        val files = fm.contentsOfDirectoryAtPath(savesDir, error = null) as? List<String> ?: return null
        val fileName = files.firstOrNull { it.removeSuffix(".txt").removeSuffix(".json").removeSuffix(".xml") == saveId } ?: return null
        val path = "$savesDir/$fileName"
        val content = NSString.create(contentsOfFile = path, encoding = NSUTF8StringEncoding, error = null)?.toString() ?: return null
        val format = when {
            fileName.endsWith(".json") -> SaveFormat.JSON
            fileName.endsWith(".xml") -> SaveFormat.XML
            else -> SaveFormat.TXT
        }
        return when (format) {
            SaveFormat.JSON -> GameSerializer.fromJson(content)
            SaveFormat.XML -> GameSerializer.fromXml(content)
            SaveFormat.TXT -> GameSerializer.fromTxt(content)
        }
    }

    actual fun deleteSave(saveId: String): Boolean {
        val fm = NSFileManager.defaultManager
        val files = fm.contentsOfDirectoryAtPath(savesDir, error = null) as? List<String> ?: return false
        val fileName = files.firstOrNull { it.removeSuffix(".txt").removeSuffix(".json").removeSuffix(".xml") == saveId } ?: return false
        return fm.removeItemAtPath("$savesDir/$fileName", error = null)
    }

    actual fun getSaveContent(saveId: String): String {
        val fm = NSFileManager.defaultManager
        val files = fm.contentsOfDirectoryAtPath(savesDir, error = null) as? List<String> ?: return "No encontrado"
        val fileName = files.firstOrNull { it.removeSuffix(".txt").removeSuffix(".json").removeSuffix(".xml") == saveId } ?: return "No encontrado"
        return NSString.create(contentsOfFile = "$savesDir/$fileName", encoding = NSUTF8StringEncoding, error = null)?.toString() ?: "Error al leer"
    }

    actual fun exportSave(saveId: String): String = getSaveContent(saveId)

    private fun parseSaveInfo(path: String, fileName: String): SaveInfo? {
        return try {
            val content = NSString.create(contentsOfFile = path, encoding = NSUTF8StringEncoding, error = null)?.toString() ?: return null
            val ext = fileName.substringAfterLast('.', "")
            val format = when (ext.lowercase()) {
                "json" -> SaveFormat.JSON
                "xml" -> SaveFormat.XML
                else -> SaveFormat.TXT
            }
            val state = when (format) {
                SaveFormat.JSON -> GameSerializer.fromJson(content)
                SaveFormat.XML -> GameSerializer.fromXml(content)
                SaveFormat.TXT -> GameSerializer.fromTxt(content)
            } ?: return null
            SaveInfo(
                saveId = fileName.substringBeforeLast('.'),
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
