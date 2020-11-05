package jp.mkjiro.reversi.data.reversi

abstract class Player(
    val name: String,
    val piece: Piece
) {
    fun putPiece(coordinate: Coordinate, board: Board) {
        board.cells[coordinate.y][coordinate.x].piece = piece
    }

    abstract fun play(board: Board)
}