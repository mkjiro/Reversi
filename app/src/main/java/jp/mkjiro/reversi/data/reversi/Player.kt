package jp.mkjiro.reversi.data.reversi

abstract class Player(
    val name: String,
    val piece: Piece
) {
    abstract fun play(playerManager: PlayerManager, board: Board)

    fun putPiece(coordinate: Coordinate, board: Board) {
        board.cells[coordinate.y][coordinate.x].piece = piece
    }

    protected var state = State.INIT

    protected enum class State {
        INIT,
        READY,
        RUNNING,
        WAITING
    }

    fun run() { state = State.RUNNING }
    fun finish() { state = State.WAITING }
}