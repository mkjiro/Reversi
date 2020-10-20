package jp.mkjiro.reversi.data.reversi

class Board(
    columns:Int = 8,
    rows:Int = 8
){
    var cells:Array<Array<Cell>> = Array(rows){
        Array(columns){
            Cell(
                CellColor.GREEN,
                Piece(PieceColor.NONE)
            )
        }
    }

    fun putPiece(coordinate: Coordinate,piece: Piece){
        cells[coordinate.y][coordinate.x].piece = piece
    }

    fun paintCell(coordinate: Coordinate, cellColor: CellColor){
        cells[coordinate.y][coordinate.x].color = cellColor
    }

    fun resetCellColor(){
        cells.forEach {
            it.forEach {
                it.color = CellColor.GREEN
            }
        }
    }
}