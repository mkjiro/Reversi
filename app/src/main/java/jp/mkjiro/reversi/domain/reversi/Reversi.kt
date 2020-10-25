package jp.mkjiro.reversi.domain.reversi

import io.reactivex.subjects.BehaviorSubject
import jp.mkjiro.reversi.data.reversi.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

interface Reversi{
    fun reset()
    fun putPiece(coordinate: Coordinate)
    fun checkCellToPutPiece()
    fun getTurnPlayerName():BehaviorSubject<String>
    fun getWinnerName():String
    fun getBoard():Board
}

@Singleton
class ReversiImpl @Inject constructor(
):Reversi{
    private val columns = 8
    private val rows = 8

    private var board : Board = Board(columns,rows)
    private var players = Array(2){
        Player(
            "Black",
            Piece(PieceColor.BLACK)
        )
        Player(
            "White",
            Piece(PieceColor.WHITE)
        )
    }
    private var turnPlayer:Player = players[0]


    private val playerName : BehaviorSubject<String> by lazy{
        BehaviorSubject.create<String>()
    }

    init {
        reset()

    }

    override fun reset() {
        board = Board(columns,rows)
        turnPlayer = players[0]

        board.putPiece(Coordinate(3,3), Piece(PieceColor.BLACK))
        board.putPiece(Coordinate(4,4), Piece(PieceColor.BLACK))
        board.putPiece(Coordinate(3,4), Piece(PieceColor.WHITE))
        board.putPiece(Coordinate(4,3), Piece(PieceColor.WHITE))

        playerName.onNext(turnPlayer.name)
    }

    override fun putPiece(coordinate: Coordinate) {
        board.putPiece(coordinate,turnPlayer.piece)
        ReversiLogic.getOverturnedPieces(coordinate,turnPlayer,board)
            .map {
                board.putPiece(it,turnPlayer.piece)
            }
        checkCellToPutPiece()
        playerName.onNext(turnPlayer.name)
    }

    override fun checkCellToPutPiece(){
        board.resetCellColor()
        ReversiLogic.getCellToPutPiece(turnPlayer,board)
            .map {
                Timber.d("%s",it)
                board.paintCell(it,CellColor.RED)
            }
    }

    override fun getTurnPlayerName(): BehaviorSubject<String> {
        return playerName
    }

    override fun getWinnerName(): String {
        TODO("Not yet implemented")
    }

    override fun getBoard():Board{
        return board
    }
}