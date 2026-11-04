package ovh.gabrielhuav.gestor

/** Almacenamiento clave-valor persistente; `actual` en cada plataforma. */
expect class PreferencesRepository() {
    fun getString(key: String, default: String = ""): String
    fun setString(key: String, value: String)
    fun getStringList(key: String): List<String>
    fun setStringList(key: String, values: List<String>)
}
