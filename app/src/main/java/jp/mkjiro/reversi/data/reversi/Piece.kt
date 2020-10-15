package jp.mkjiro.reversi.data.reversi

data class Piece(
    var color : Color = Color.NONE
)

enum class Color{
    NONE,
    BLACK,
    WHITE
}
