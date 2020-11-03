package jp.mkjiro.reversi.domain.reversi

import io.reactivex.processors.PublishProcessor
import io.reactivex.subjects.BehaviorSubject
import jp.mkjiro.reversi.data.reversi.*
import timber.log.Timber

class Reversi(
    private val board: Board,
    private val players: Array<Player>
) {
    private var turnPlayerIndex = 0
    private var turnPlayer: Player = players[turnPlayerIndex]
    private val playerName: BehaviorSubject<String> by lazy {
        BehaviorSubject.create<String>()
    }
    private val winnerPlayerName: PublishProcessor<String> by lazy {
        PublishProcessor.create<String>()
    }
    private var cellsToPutPiece: Array<Coordinate> = arrayOf()

    init {
        reset()
    }

    fun reset() {
        turnPlayerIndex = 0
        turnPlayer = players[turnPlayerIndex]

        board.putPiece(Coordinate(3, 3), Piece(PieceColor.BLACK))
        board.putPiece(Coordinate(4, 4), Piece(PieceColor.BLACK))
        board.putPiece(Coordinate(3, 4), Piece(PieceColor.WHITE))
        board.putPiece(Coordinate(4, 3), Piece(PieceColor.WHITE))

        paintCellsToPuPiece()
        playerName.onNext(turnPlayer.name)
    }

    fun putPiece(coordinate: Coordinate) {
        Timber.d("coordinate to put : %s", coordinate)
        //駒が置ける場所かチェック
        if (!cellsToPutPiece.contains(coordinate))return //置けない場所
        //ボードに駒を置く
        board.putPiece(coordinate, turnPlayer.piece)
        //ひっくり返す
        ReversiLogic.getOverturnedPieces(coordinate, turnPlayer, board)
            .map {
                board.putPiece(it, turnPlayer.piece)
            }
        //ターンプレイヤーを変更
        turnPlayer = players[++turnPlayerIndex % players.size]
        Timber.d("%s", turnPlayer)
        //セルの色をリセット
        board.resetCellColor()
        //駒が置けるかチェック
        if (paintCellsToPuPiece().isEmpty()) {
            //ターンプレイヤーを変更
            turnPlayer = players[++turnPlayerIndex % players.size]
            if (paintCellsToPuPiece().isEmpty()) {
                //ゲーム終了
                winnerPlayerName.onNext(
                    ReversiLogic.getWinnerName(board, players)
                )
            } else {
                playerName.onNext(turnPlayer.name)
            }
        } else {
            playerName.onNext(turnPlayer.name)
        }
    }

    private fun paintCellsToPuPiece(): Array<Coordinate> {
        val cells = ReversiLogic.getCellToPutPiece(turnPlayer, board)
        if (cells.isNotEmpty()) {
            cells.map {
                Timber.d("%s", it)
                board.paintCell(it, CellColor.RED)
            }
            cellsToPutPiece = cells
        }
        return cells
    }

    fun getTurnPlayerName(): BehaviorSubject<String> {
        return playerName
    }

    fun getWinnerName(): PublishProcessor<String> {
        return winnerPlayerName
    }

    fun getBoard(): Board {
        return board
    }
}