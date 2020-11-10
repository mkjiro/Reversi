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

    private var state = State.INIT

    suspend fun reset() {
        board.resetPiece()
        board.resetCellColor()
        changeState(State.START)
    }

    suspend fun putPiece(coordinate: Coordinate) {
        Timber.d("coordinate to put : %s", coordinate)
        Timber.d("%s", state)
        if (state != State.TURN_OF_HUMAN)return
        reversePiece(coordinate)
    }

    private suspend fun reversePiece(coordinate: Coordinate) {
        //駒が置ける場所かチェック
        if (!cellsToPutPiece.contains(coordinate))return //置けない場所
        changeState(State.PROCESSING)
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
        changeState(State.JUDGE)
    }

    private suspend fun changeState(state: State) {
        this.state = state
        onStateChanged()
    }

    private suspend fun onStateChanged() {
        Timber.d("state: $state")
        when (state) {
            State.INIT -> {
                reset()
            }
            State.START -> {
                changeState(State.JUDGE)
            }
            State.JUDGE -> {
                if (isContinued()) {
                    changeState(State.PLAYER)
                } else {
                    changeState(State.FINISH)
                }
            }
            State.PLAYER -> {
                processTurnPlayer()
            }
            State.PROCESSING -> {
            }
            State.FINISH -> {
            }
            else -> {

            }
        }
    }

    private fun isContinued(): Boolean {
        //駒が置けるかチェック
        var cells = ReversiLogic.getCellToPutPiece(playerManager.turnPlayer, board)
        if (cells.isEmpty()) {
            //ターンプレイヤーを変更
            playerManager.alternateTurnPlayer()
            cells = ReversiLogic.getCellToPutPiece(playerManager.turnPlayer, board)
            if (cells.isEmpty()) {
                //ゲーム終了
                winnerPlayerName.onNext(
                    ReversiLogic.getWinnerName(board, playerManager.players)
                )
                return false
            } else {
                cellsToPutPiece = cells
                paintCellsToPuPiece(cells)
                playerName.onNext(playerManager.turnPlayer.name)
            }
        } else {
            cellsToPutPiece = cells
            paintCellsToPuPiece(cells)
            playerName.onNext(playerManager.turnPlayer.name)
        }
        return true
    }

    private suspend fun processTurnPlayer() {
        when (val player = playerManager.turnPlayer) {
            is CPU -> {
                state = State.TURN_OF_CPU
                delay(1000)
                player.play(board)
                playerManager.alternateTurnPlayer()
                //セルの色をリセット
                board.resetCellColor()
                changeState(State.JUDGE)
            }
            is Human -> {
                state = State.TURN_OF_HUMAN
            }
        }
    }

    private fun paintCellsToPuPiece(cells: Array<Coordinate>) {
        if (cells.isNotEmpty()) {
            cells.map {
                Timber.d("%s", it)
                board.paintCell(it, CellColor.RED)
            }
        }
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
    START,
    JUDGE,
    PROCESSING,
    PLAYER,
    TURN_OF_HUMAN,
    TURN_OF_CPU,
    FINISH
}