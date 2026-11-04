package ovh.gabrielhuav.gestor

private const val KEY_RECENT = "recent_files"
private const val KEY_FAVORITES = "favorite_files"
private const val MAX_RECENT = 20

class RecentFilesManager(private val prefs: PreferencesRepository) {

    // ---- Historial de recientes ----

    fun getRecent(): List<String> = prefs.getStringList(KEY_RECENT)

    fun addRecent(path: String) {
        val current = getRecent().toMutableList()
        current.remove(path)
        current.add(0, path)
        if (current.size > MAX_RECENT) current.removeAt(current.lastIndex)
        prefs.setStringList(KEY_RECENT, current)
    }

    fun clearRecent() = prefs.setStringList(KEY_RECENT, emptyList())

    // ---- Favoritos ----

    fun getFavorites(): List<String> = prefs.getStringList(KEY_FAVORITES)

    fun addFavorite(path: String) {
        val current = getFavorites().toMutableList()
        if (!current.contains(path)) {
            current.add(path)
            prefs.setStringList(KEY_FAVORITES, current)
        }
    }

    fun removeFavorite(path: String) {
        val current = getFavorites().toMutableList()
        current.remove(path)
        prefs.setStringList(KEY_FAVORITES, current)
    }

    fun isFavorite(path: String): Boolean = getFavorites().contains(path)
}
