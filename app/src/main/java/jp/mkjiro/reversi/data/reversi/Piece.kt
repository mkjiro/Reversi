package jp.mkjiro.reversi.data.reversi

data class Piece(
    val color: PieceColor = PieceColor.NONE
)

enum class PieceColor {
    NONE,
    BLACK,
    WHITE
}
