package ovh.gabrielhuav.gestor

import android.content.Context
import android.content.SharedPreferences

object AndroidPreferences {
    lateinit var sharedPreferences: SharedPreferences
}

actual class PreferencesRepository {
    private val prefs get() = AndroidPreferences.sharedPreferences

    actual fun getString(key: String, default: String): String =
        prefs.getString(key, default) ?: default

    actual fun setString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    actual fun getStringList(key: String): List<String> {
        val str = prefs.getString(key, "") ?: ""
        return if (str.isEmpty()) emptyList() else str.split("|||")
    }

    actual fun setStringList(key: String, values: List<String>) {
        prefs.edit().putString(key, values.joinToString("|||")).apply()
    }
}
