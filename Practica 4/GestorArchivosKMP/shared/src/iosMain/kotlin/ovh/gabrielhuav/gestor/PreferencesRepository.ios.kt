package ovh.gabrielhuav.gestor

import platform.Foundation.NSUserDefaults

actual class PreferencesRepository {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual fun getString(key: String, default: String): String =
        defaults.stringForKey(key) ?: default

    actual fun setString(key: String, value: String) {
        defaults.setObject(value, forKey = key)
    }

    actual fun getStringList(key: String): List<String> {
        val str = defaults.stringForKey(key) ?: ""
        return if (str.isEmpty()) emptyList() else str.split("|||")
    }

    actual fun setStringList(key: String, values: List<String>) {
        defaults.setObject(values.joinToString("|||"), forKey = key)
    }
}
