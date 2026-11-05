package ovh.gabrielhuav.juego

/**
 * Serializa/deserializa el estado del juego a TXT, XML y JSON.
 * Implementación pura en Kotlin (sin librerías externas).
 */
object GameSerializer {

    // ============ SERIALIZAR ============

    fun toTxt(state: GameState): String {
        val sb = StringBuilder()
        sb.appendLine("=== PARTIDA 2048 KMP ===")
        sb.appendLine("nivel=${state.level}")
        sb.appendLine("puntuacion=${state.score}")
        sb.appendLine("tiempo=${state.elapsedSeconds}")
        sb.appendLine("tag=${state.tag}")
        sb.appendLine("guardado=${state.savedAt}")
        sb.appendLine("ganado=${state.hasWon}")
        sb.appendLine("game_over=${state.isGameOver}")
        sb.appendLine("--- TABLERO ---")
        state.board.forEach { row ->
            sb.appendLine(row.joinToString(","))
        }
        sb.appendLine("--- HISTORIAL ---")
        state.moveHistory.forEach { move -> sb.appendLine(move) }
        return sb.toString()
    }

    fun toJson(state: GameState): String {
        val boardJson = state.board.joinToString(",\n    ") { row ->
            "[${row.joinToString(", ")}]"
        }
        val historyJson = state.moveHistory.joinToString(", ") { "\"$it\"" }
        return """{
  "version": "1.0",
  "nivel": ${state.level},
  "puntuacion": ${state.score},
  "tiempo": ${state.elapsedSeconds},
  "tag": "${state.tag}",
  "guardado": ${state.savedAt},
  "ganado": ${state.hasWon},
  "game_over": ${state.isGameOver},
  "tablero": [
    $boardJson
  ],
  "historial": [$historyJson]
}"""
    }

    fun toXml(state: GameState): String {
        val sb = StringBuilder()
        sb.appendLine("""<?xml version="1.0" encoding="UTF-8"?>""")
        sb.appendLine("<partida version=\"1.0\">")
        sb.appendLine("  <nivel>${state.level}</nivel>")
        sb.appendLine("  <puntuacion>${state.score}</puntuacion>")
        sb.appendLine("  <tiempo>${state.elapsedSeconds}</tiempo>")
        sb.appendLine("  <tag>${escapeXml(state.tag)}</tag>")
        sb.appendLine("  <guardado>${state.savedAt}</guardado>")
        sb.appendLine("  <ganado>${state.hasWon}</ganado>")
        sb.appendLine("  <game_over>${state.isGameOver}</game_over>")
        sb.appendLine("  <tablero>")
        state.board.forEachIndexed { rowIdx, row ->
            sb.append("    <fila index=\"$rowIdx\">")
            row.forEach { v -> sb.append("<celda>$v</celda>") }
            sb.appendLine("</fila>")
        }
        sb.appendLine("  </tablero>")
        sb.appendLine("  <historial>")
        state.moveHistory.forEach { move ->
            sb.appendLine("    <movimiento>$move</movimiento>")
        }
        sb.appendLine("  </historial>")
        sb.appendLine("</partida>")
        return sb.toString()
    }

    // ============ DESERIALIZAR ============

    fun fromTxt(text: String): GameState? {
        return try {
            val lines = text.lines()
            var nivel = 1; var score = 0L; var tiempo = 0L
            var tag = ""; var guardado = 0L; var ganado = false; var gameOver = false
            val boardRows = mutableListOf<List<Int>>()
            val history = mutableListOf<String>()
            var mode = "header"

            for (line in lines) {
                when {
                    line.startsWith("nivel=") -> nivel = line.substringAfter("=").trim().toInt()
                    line.startsWith("puntuacion=") -> score = line.substringAfter("=").trim().toLong()
                    line.startsWith("tiempo=") -> tiempo = line.substringAfter("=").trim().toLong()
                    line.startsWith("tag=") -> tag = line.substringAfter("=").trim()
                    line.startsWith("guardado=") -> guardado = line.substringAfter("=").trim().toLong()
                    line.startsWith("ganado=") -> ganado = line.substringAfter("=").trim().toBoolean()
                    line.startsWith("game_over=") -> gameOver = line.substringAfter("=").trim().toBoolean()
                    line == "--- TABLERO ---" -> mode = "board"
                    line == "--- HISTORIAL ---" -> mode = "history"
                    mode == "board" && line.contains(",") -> {
                        boardRows.add(line.split(",").map { it.trim().toInt() })
                    }
                    mode == "history" && line.isNotBlank() && line != "=== PARTIDA 2048 KMP ===" -> {
                        try { Direction.valueOf(line.trim()); history.add(line.trim()) } catch (_: Exception) {}
                    }
                }
            }
            GameState(board = boardRows, score = score, elapsedSeconds = tiempo,
                level = nivel, isGameOver = gameOver, hasWon = ganado,
                moveHistory = history, tag = tag, savedAt = guardado)
        } catch (e: Exception) { null }
    }

    fun fromJson(json: String): GameState? {
        return try {
            fun extractLong(key: String) = Regex("\"$key\"\\s*:\\s*(-?\\d+)").find(json)?.groupValues?.get(1)?.toLong() ?: 0L
            fun extractBool(key: String) = Regex("\"$key\"\\s*:\\s*(true|false)").find(json)?.groupValues?.get(1)?.toBoolean() ?: false
            fun extractStr(key: String) = Regex("\"$key\"\\s*:\\s*\"([^\"]*)\"").find(json)?.groupValues?.get(1) ?: ""

            val nivel = extractLong("nivel").toInt()
            val score = extractLong("puntuacion")
            val tiempo = extractLong("tiempo")
            val guardado = extractLong("guardado")
            val tag = extractStr("tag")
            val ganado = extractBool("ganado")
            val gameOver = extractBool("game_over")

            // Parsear tablero: arrays de arrays
            val tableroRegex = Regex("\\[([\\d,\\s]+)\\]")
            val boardRows = tableroRegex.findAll(json).toList()
                .filter { it.value.contains(",") }  // filas con múltiples valores
                .map { match -> match.groupValues[1].split(",").map { it.trim().toInt() } }

            // Historial
            val historyRegex = Regex("\"(UP|DOWN|LEFT|RIGHT)\"")
            val history = historyRegex.findAll(json).map { it.groupValues[1] }.toList()

            GameState(board = boardRows, score = score, elapsedSeconds = tiempo,
                level = nivel, isGameOver = gameOver, hasWon = ganado,
                moveHistory = history, tag = tag, savedAt = guardado)
        } catch (e: Exception) { null }
    }

    fun fromXml(xml: String): GameState? {
        return try {
            fun extractTag(tag: String) = Regex("<$tag>([^<]*)</$tag>").find(xml)?.groupValues?.get(1) ?: ""

            val nivel = extractTag("nivel").trim().toInt()
            val score = extractTag("puntuacion").trim().toLong()
            val tiempo = extractTag("tiempo").trim().toLong()
            val tag = extractTag("tag").trim()
            val guardado = extractTag("guardado").trim().toLong()
            val ganado = extractTag("ganado").trim().toBoolean()
            val gameOver = extractTag("game_over").trim().toBoolean()

            // Parsear filas del tablero
            val filaRegex = Regex("<fila[^>]*>(.*?)</fila>", RegexOption.DOT_MATCHES_ALL)
            val celdaRegex = Regex("<celda>(\\d+)</celda>")
            val boardRows = filaRegex.findAll(xml).map { filaMatch ->
                celdaRegex.findAll(filaMatch.groupValues[1]).map { it.groupValues[1].toInt() }.toList()
            }.toList()

            // Historial
            val movRegex = Regex("<movimiento>([A-Z]+)</movimiento>")
            val history = movRegex.findAll(xml).map { it.groupValues[1] }.toList()

            GameState(board = boardRows, score = score, elapsedSeconds = tiempo,
                level = nivel, isGameOver = gameOver, hasWon = ganado,
                moveHistory = history, tag = tag, savedAt = guardado)
        } catch (e: Exception) { null }
    }

    private fun escapeXml(s: String) = s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")

    fun detectFormat(content: String): SaveFormat = when {
        content.trimStart().startsWith("{") -> SaveFormat.JSON
        content.trimStart().startsWith("<") -> SaveFormat.XML
        else -> SaveFormat.TXT
    }
}
