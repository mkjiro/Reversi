package jp.mkjiro.reversi.domain.reversi.board

class Board(
    val columns: Int = 8,
    val rows: Int = 8
) {
    var cells: Array<Array<Cell>> = Array(rows) {
        Array(columns) {
            Cell(
                CellColor.GREEN,
                Piece(PieceColor.NONE)
            )
        }
    }

    fun resetPiece() {
        putPiece(
            Coordinate(3, 3),
            Piece(PieceColor.BLACK)
        )
        putPiece(
            Coordinate(4, 4),
            Piece(PieceColor.BLACK)
        )
        putPiece(
            Coordinate(3, 4),
            Piece(PieceColor.WHITE)
        )
        putPiece(
            Coordinate(4, 3),
            Piece(PieceColor.WHITE)
        )
    }

    fun putPiece(coordinate: Coordinate, piece: Piece) {
        cells[coordinate.y][coordinate.x].piece = piece
    }

    fun paintCell(coordinate: Coordinate, cellColor: CellColor) {
        cells[coordinate.y][coordinate.x].color = cellColor
    }

    fun resetCellColor() {
        cells.forEach {
            it.forEach {
                it.color = CellColor.GREEN
            }
        }
    }

    fun copy(): Board {
        val board = Board()
        board.cells = Array(this.rows) { y ->
            Array(this.columns) { x ->
                this.cells[y][x].copy()
            }
        }
        return board
    }
}