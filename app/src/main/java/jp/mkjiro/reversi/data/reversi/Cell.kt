package jp.mkjiro.reversi.data.reversi

data class Cell(
    var color : CellColor,
    var piece : Piece
)

enum class CellColor{
    NONE,
    GREEN,
    RED
}