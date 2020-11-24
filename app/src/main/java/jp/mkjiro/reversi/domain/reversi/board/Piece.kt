package jp.mkjiro.reversi.reversi.board

data class Piece(
    val color: PieceColor = PieceColor.NONE
)

enum class PieceColor {
    NONE,
    BLACK,
    WHITE
}
