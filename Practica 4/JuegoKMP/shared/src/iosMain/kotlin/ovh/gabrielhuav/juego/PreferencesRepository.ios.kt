package ovh.gabrielhuav.juego

import platform.Foundation.NSUserDefaults

actual class PreferencesRepository {
    private val defaults = NSUserDefaults.standardUserDefaults
    actual fun getString(key: String, default: String): String = defaults.stringForKey(key) ?: default
    actual fun setString(key: String, value: String) { defaults.setObject(value, forKey = key) }
}
