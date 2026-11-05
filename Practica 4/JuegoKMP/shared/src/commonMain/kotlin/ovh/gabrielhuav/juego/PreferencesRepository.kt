package ovh.gabrielhuav.juego

expect class PreferencesRepository() {
    fun getString(key: String, default: String = ""): String
    fun setString(key: String, value: String)
}
