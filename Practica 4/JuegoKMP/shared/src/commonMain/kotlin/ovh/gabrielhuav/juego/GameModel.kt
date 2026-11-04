package ovh.gabrielhuav.juego

import kotlin.random.Random

/** Dirección de deslizamiento */
enum class Direction { UP, DOWN, LEFT, RIGHT }

/** Estado completo de una partida */
data class GameState(
    val board: List<List<Int>>,        // board[row][col], 0 = vacío
    val score: Long,
    val elapsedSeconds: Long,
    val level: Int,                    // 1 = 4x4, 2 = 5x5
    val isGameOver: Boolean,
    val hasWon: Boolean,
    val moveHistory: List<String>,     // últimos 20 movimientos
    val tag: String = "",              // etiqueta del usuario
    val savedAt: Long = 0L,            // epoch millis
    val saveId: String = "",           // nombre del archivo sin extensión
)

/** Lógica de negocio del juego 2048 */
object GameEngine {

    fun newGame(level: Int): GameState {
        val size = boardSize(level)
        var board = emptyBoard(size)
        board = addRandomTile(board)
        board = addRandomTile(board)
        return GameState(
            board = board,
            score = 0L,
            elapsedSeconds = 0L,
            level = level,
            isGameOver = false,
            hasWon = false,
            moveHistory = emptyList()
        )
    }

    fun boardSize(level: Int) = if (level >= 2) 5 else 4

    fun slide(state: GameState, dir: Direction): GameState {
        val (newBoard, gained) = slideBoard(state.board, dir)
        if (newBoard == state.board) return state  // sin cambios

        val withTile = addRandomTile(newBoard)
        val newScore = state.score + gained
        val gameOver = isGameOver(withTile)
        val won = hasWon(withTile)
        val history = (state.moveHistory + dir.name).takeLast(20)

        return state.copy(
            board = withTile,
            score = newScore,
            isGameOver = gameOver,
            hasWon = won,
            moveHistory = history
        )
    }

    private fun slideBoard(board: List<List<Int>>, dir: Direction): Pair<List<List<Int>>, Long> {
        // Rotamos para trabajar siempre como "slide left"
        val rotated = when (dir) {
            Direction.LEFT -> board
            Direction.RIGHT -> board.map { it.reversed() }
            Direction.UP -> transpose(board)
            Direction.DOWN -> transpose(board).map { it.reversed() }
        }
        var totalGained = 0L
        val slid = rotated.map { row ->
            val (newRow, gained) = slideRowLeft(row)
            totalGained += gained
            newRow
        }
        // Desrotamos
        val result = when (dir) {
            Direction.LEFT -> slid
            Direction.RIGHT -> slid.map { it.reversed() }
            Direction.UP -> transpose(slid)
            Direction.DOWN -> transpose(slid.map { it.reversed() })
        }
        return result to totalGained
    }

    private fun slideRowLeft(row: List<Int>): Pair<List<Int>, Long> {
        val nonZero = row.filter { it != 0 }.toMutableList()
        var gained = 0L
        var i = 0
        while (i < nonZero.size - 1) {
            if (nonZero[i] == nonZero[i + 1]) {
                nonZero[i] *= 2
                gained += nonZero[i]
                nonZero.removeAt(i + 1)
            }
            i++
        }
        val result = nonZero + List(row.size - nonZero.size) { 0 }
        return result to gained
    }

    private fun transpose(board: List<List<Int>>): List<List<Int>> {
        val size = board.size
        return List(size) { row -> List(size) { col -> board[col][row] } }
    }

    private fun emptyBoard(size: Int) = List(size) { List(size) { 0 } }

    fun addRandomTile(board: List<List<Int>>): List<List<Int>> {
        val empties = mutableListOf<Pair<Int, Int>>()
        board.forEachIndexed { r, row ->
            row.forEachIndexed { c, v -> if (v == 0) empties.add(r to c) }
        }
        if (empties.isEmpty()) return board
        val (r, c) = empties[Random.nextInt(empties.size)]
        val value = if (Random.nextFloat() < 0.9f) 2 else 4
        return board.mapIndexed { ri, row ->
            if (ri != r) row else row.mapIndexed { ci, v -> if (ci == c) value else v }
        }
    }

    private fun isGameOver(board: List<List<Int>>): Boolean {
        // Si hay un 0, no es game over
        if (board.any { row -> row.any { it == 0 } }) return false
        // Si hay una fusión posible, no es game over
        val size = board.size
        for (r in 0 until size) {
            for (c in 0 until size) {
                if (c + 1 < size && board[r][c] == board[r][c + 1]) return false
                if (r + 1 < size && board[r][c] == board[r + 1][c]) return false
            }
        }
        return true
    }

    private fun hasWon(board: List<List<Int>>) = board.any { row -> row.any { it >= 2048 } }

    fun updateTime(state: GameState, elapsedSeconds: Long) = state.copy(elapsedSeconds = elapsedSeconds)
}
