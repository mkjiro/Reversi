package jp.mkjiro.reversi.domain.reversi

import io.reactivex.processors.PublishProcessor
import io.reactivex.subjects.BehaviorSubject
import jp.mkjiro.reversi.data.reversi.*
import kotlinx.coroutines.*
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

    var state = State.INIT

    suspend fun reset() {
        board.resetPiece()
        board.resetCellColor()
        judePhase()
    }

    suspend fun putPiece(coordinate: Coordinate) {
        Timber.d("coordinate to put : %s", coordinate)
        Timber.d("%s", state)
        if (state != State.TURN_OF_HUMAN)return
        //駒が置ける場所かチェック
        if (!cellsToPutPiece.contains(coordinate))return //置けない場所
        state = State.PROCESSING
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
        judePhase()
    }

    private fun isContinued(): Boolean {
        //駒が置けるかチェック
        if (paintCellsToPuPiece().isEmpty()) {
            //ターンプレイヤーを変更
            playerManager.alternateTurnPlayer()
            if (paintCellsToPuPiece().isEmpty()) {
                //ゲーム終了
                winnerPlayerName.onNext(
                    ReversiLogic.getWinnerName(board, playerManager.players)
                )
                return false
            } else {
                playerName.onNext(playerManager.turnPlayer.name)
            }
        } else {
            playerName.onNext(playerManager.turnPlayer.name)
        }
        return true
    }

    private suspend fun processTurnPlayer() {
        val player = playerManager.turnPlayer
        if (player is CPU) {
            state = State.TURN_OF_CPU
            delay(1000)
            player.play(board)
            playerManager.alternateTurnPlayer()
            //セルの色をリセット
            board.resetCellColor()
            judePhase()
        } else if (player is Human) {
            state = State.TURN_OF_HUMAN
        }
    }

    private suspend fun judePhase() {
        if (isContinued()) {
            processTurnPlayer()
        } else {
            state = State.FINISH
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

enum class State {
    INIT,
    PROCESSING,
    TURN_OF_HUMAN,
    TURN_OF_CPU,
    FINISH
}