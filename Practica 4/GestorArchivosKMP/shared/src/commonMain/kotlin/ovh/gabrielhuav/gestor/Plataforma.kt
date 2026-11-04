package ovh.gabrielhuav.gestor

/** Nombre y versión del sistema. */
expect fun plataforma(): String

/** Formatea un timestamp epoch en milisegundos a una cadena legible. */
expect fun formatEpoch(epochMillis: Long): String
