package jp.mkjiro.reversi.data.reversi

data class Piece(
    var color : PieceColor = PieceColor.NONE
)

enum class PieceColor{
    NONE,
    BLACK,
    WHITE
}
