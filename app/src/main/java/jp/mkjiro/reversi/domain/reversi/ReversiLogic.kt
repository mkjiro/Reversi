package jp.mkjiro.reversi.domain.reversi

import jp.mkjiro.reversi.data.reversi.*


data class Direction(
    val y: Int = 0,
    val x: Int = 0
)

object ReversiLogic{
    fun getCellToPutPiece(
        player: Player,
        board: Board
    ):Array<Coordinate>{
        var cells = arrayOf<Coordinate>()
        board.cells.mapIndexed { y, arrayOfPieces ->
            arrayOfPieces.mapIndexed{x, cell ->
                if(cell.piece.color == PieceColor.NONE){
                    if(getOverturnedPieces(Coordinate(y, x), player, board).isNotEmpty())
                        cells += Coordinate(y,x)
                }
            }
        }
        return cells
    }

    fun getOverturnedPieces(
        newPos:Coordinate,
        player: Player,
        board: Board
    ):Array<Coordinate>{
        var cells = arrayOf<Coordinate>()
        for(y in -1..1){
            for(x in -1..1){
                if(y == 0 && x == 0)continue
                cells += getOverturnedPiecesOnLine(Direction(y,x),newPos,player,board)
            }
        }
        return cells
    }

    fun getWinner(board: Board, players: Array<Player>):Player{
        return players[0]
    }

    private fun isRange(y: Int, x: Int,board: Board):Boolean{
        return (y in board.cells.indices) && (x in board.cells[0].indices)
    }

    private fun getOverturnedPiecesOnLine(
        dir:Direction,
        newPos:Coordinate,
        player:Player,
        board: Board
    ):Array<Coordinate>{
        var cells = arrayOf<Coordinate>()
        var y = newPos.y + dir.y
        var x = newPos.x + dir.x
        loop@ while(isRange(y,x,board)){
            when(board.cells[y][x].piece.color){
                player.piece.color -> break@loop
                PieceColor.NONE -> return arrayOf()
                else -> cells += Coordinate(y,x)
            }
            y+=dir.y
            x+=dir.x
        }
        if(!isRange(y,x,board)){
            return arrayOf()
        }

        return cells
    }
}