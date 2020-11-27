package jp.mkjiro.reversi.domain.reversi.board

data class Piece(
    val color: PieceColor
)

enum class PieceColor {
    BLACK,
    WHITE
}
