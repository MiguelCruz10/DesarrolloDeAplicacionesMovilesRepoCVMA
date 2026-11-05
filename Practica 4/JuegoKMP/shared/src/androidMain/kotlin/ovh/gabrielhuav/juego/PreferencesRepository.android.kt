package ovh.gabrielhuav.juego

import android.content.SharedPreferences

object AndroidPreferences {
    lateinit var sharedPreferences: SharedPreferences
}

actual class PreferencesRepository {
    private val prefs get() = AndroidPreferences.sharedPreferences
    actual fun getString(key: String, default: String): String = prefs.getString(key, default) ?: default
    actual fun setString(key: String, value: String) { prefs.edit().putString(key, value).apply() }
}
