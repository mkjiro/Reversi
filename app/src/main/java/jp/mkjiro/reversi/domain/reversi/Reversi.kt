package jp.mkjiro.reversi.domain.reversi

import jp.mkjiro.reversi.data.reversi.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

interface Reversi{
    fun reset()
    fun putPiece(coordinate: Coordinate)
    fun getCellToPutPiece():Array<Coordinate>
    fun getTurnPlayerName():String
    fun getWinnerName():String
    fun getBoard():Board
}

@Singleton
class ReversiImpl @Inject constructor(
):Reversi{
    private var board : Board = Board(8,8)
    private var players = Array(2){
        Player(Piece(PieceColor.BLACK))
        Player(Piece(PieceColor.WHITE))
    }
    private var turnPlayer:Player = players[0]

    init {
        reset()
    }

    override fun reset() {
        board = Board(8,8)
        turnPlayer = players[0]

        board.putPiece(Coordinate(3,3), Piece(PieceColor.BLACK))
        board.putPiece(Coordinate(4,4), Piece(PieceColor.BLACK))
        board.putPiece(Coordinate(3,4), Piece(PieceColor.WHITE))
        board.putPiece(Coordinate(4,3), Piece(PieceColor.WHITE))
    }

    override fun putPiece(coordinate: Coordinate) {
        board.putPiece(coordinate,turnPlayer.piece)
        ReversiLogic.getOverturnedPieces(coordinate,turnPlayer,board)
            .map {
                Timber.d("%s %s",it.y,it.x)
                board.putPiece(it,turnPlayer.piece)
            }
    }

    override fun getCellToPutPiece(): Array<Coordinate> {
        return ReversiLogic.getCellToPutPiece(turnPlayer,board)
    }

    override fun getTurnPlayerName(): String {
        TODO("Not yet implemented")
    }

    override fun getWinnerName(): String {
        TODO("Not yet implemented")
    }

    override fun getBoard():Board{
        return board
    }
}