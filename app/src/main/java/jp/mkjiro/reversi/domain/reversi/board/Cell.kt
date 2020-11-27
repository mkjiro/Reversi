package jp.mkjiro.reversi.domain.reversi.board

data class Cell(
    var color: CellColor,
    var piece: Piece?
)

enum class CellColor {
    GREEN,
    RED
}