package jp.mkjiro.reversi.domain.reversi

import io.reactivex.processors.PublishProcessor
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
    fun getWinnerName():PublishProcessor<String>
    fun getBoard():Board
}

@Singleton
class ReversiImpl @Inject constructor(
):Reversi{
    private val columns = 8
    private val rows = 8

    private var board : Board = Board(columns,rows)
    private var players = arrayOf(
        Player(
            "Black",
            Piece(PieceColor.BLACK)
        ),
        Player(
            "White",
            Piece(PieceColor.WHITE)
        )
    )
    private var turnPlayerIndex = 0
    private var turnPlayer:Player = players[turnPlayerIndex]
    private val playerName : BehaviorSubject<String> by lazy{
        BehaviorSubject.create<String>()
    }
    private val winnerPlayerName : PublishProcessor<String> by lazy{
        PublishProcessor.create<String>()
    }
    private var cellsToPutPiece : Array<Coordinate> = arrayOf()

    init {
        reset()
    }

    override fun reset() {
        board = Board(columns,rows)
        turnPlayerIndex = 0
        turnPlayer = players[turnPlayerIndex]

        board.putPiece(Coordinate(3,3), Piece(PieceColor.BLACK))
        board.putPiece(Coordinate(4,4), Piece(PieceColor.BLACK))
        board.putPiece(Coordinate(3,4), Piece(PieceColor.WHITE))
        board.putPiece(Coordinate(4,3), Piece(PieceColor.WHITE))

        paintCellsToPuPiece()
        playerName.onNext(turnPlayer.name)
    }

    override fun putPiece(coordinate: Coordinate) {
        Timber.d("coordinate to put : %s", coordinate)
        //駒が置ける場所かチェック
        if(!cellsToPutPiece.contains(coordinate))return //置けない場所
        //ボードに駒を置く
        board.putPiece(coordinate,turnPlayer.piece)
        //ひっくり返す
        ReversiLogic.getOverturnedPieces(coordinate,turnPlayer,board)
            .map {
                board.putPiece(it,turnPlayer.piece)
            }
        //ターンプレイヤーを変更
        turnPlayer = players[++turnPlayerIndex%players.size]
        Timber.d("%s",turnPlayer)
        //セルの色をリセット
        board.resetCellColor()
        //駒が置けるかチェック
        if(paintCellsToPuPiece().isEmpty()){
            //ターンプレイヤーを変更
            turnPlayer = players[++turnPlayerIndex%players.size]
            if(paintCellsToPuPiece().isEmpty()) {
                //ゲーム終了
                winnerPlayerName.onNext(
                    ReversiLogic.getWinner(board, players).name
                )
            }else{
                playerName.onNext(turnPlayer.name)
            }
        }else{
            playerName.onNext(turnPlayer.name)
        }
    }

    private fun paintCellsToPuPiece():Array<Coordinate>{
        val cells = ReversiLogic.getCellToPutPiece(turnPlayer,board)
        if(cells.isNotEmpty()){
            cells.map {
                Timber.d("%s",it)
                board.paintCell(it,CellColor.RED)
            }
            cellsToPutPiece = cells
        }
        return cells
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

    override fun getWinnerName(): PublishProcessor<String> {
        return winnerPlayerName
    }

    override fun getBoard():Board{
        return board
    }
}