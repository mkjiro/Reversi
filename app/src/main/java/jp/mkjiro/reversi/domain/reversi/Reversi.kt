package jp.mkjiro.reversi.domain.reversi

import io.reactivex.processors.PublishProcessor
import io.reactivex.subjects.BehaviorSubject
import jp.mkjiro.reversi.data.reversi.*
import timber.log.Timber

class Reversi(
    private val board: Board,
    private val playerManager: PlayerManager
) {
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

    private fun reset() {
        board.resetPiece()
        board.resetCellColor()
        paintCellsToPuPiece()
        playerName.onNext(playerManager.turnPlayer.name)
    }

    fun putPiece(coordinate: Coordinate) {
        Timber.d("coordinate to put : %s", coordinate)
        //駒が置ける場所かチェック
        if (!cellsToPutPiece.contains(coordinate))return //置けない場所
        //ボードに駒を置く
        playerManager.turnPlayer.putPiece(coordinate, board)
        //ひっくり返す
        ReversiLogic.getOverturnedPieces(coordinate, playerManager.turnPlayer, board)
            .map {
                playerManager.turnPlayer.putPiece(it, board)
            }
        //ターンプレイヤーを変更
        playerManager.alternateTurnPlayer()
        Timber.d("%s", playerManager.turnPlayer)
        //セルの色をリセット
        board.resetCellColor()
        //駒が置けるかチェック
        if (paintCellsToPuPiece().isEmpty()) {
            //ターンプレイヤーを変更
            playerManager.alternateTurnPlayer()
            if (paintCellsToPuPiece().isEmpty()) {
                //ゲーム終了
                winnerPlayerName.onNext(
                    ReversiLogic.getWinnerName(board, playerManager.players)
                )
            } else {
                playerName.onNext(playerManager.turnPlayer.name)
            }
        } else {
            playerName.onNext(playerManager.turnPlayer.name)
        }
    }

    private fun paintCellsToPuPiece(): Array<Coordinate> {
        val cells = ReversiLogic.getCellToPutPiece(playerManager.turnPlayer, board)
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