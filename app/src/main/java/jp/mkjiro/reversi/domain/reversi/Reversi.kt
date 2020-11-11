package jp.mkjiro.reversi.domain.reversi

import io.reactivex.processors.PublishProcessor
import io.reactivex.subjects.BehaviorSubject
import jp.mkjiro.reversi.data.reversi.*
import timber.log.Timber

class Reversi(
    private val board: Board,
    private val playerManager: PlayerManager
) : ReversiStateMachine() {
    private val playerName: BehaviorSubject<String> by lazy {
        BehaviorSubject.create<String>()
    }
    private val winnerPlayerName: PublishProcessor<String> by lazy {
        PublishProcessor.create<String>()
    }
    private var cellsToPutPiece: Array<Coordinate> = arrayOf()

    init {
        changeState(State.INIT)
    }

    fun putPiece(coordinate: Coordinate) {
        Timber.d("coordinate to put : %s", coordinate)
        Timber.d("%s", state)
        if (state != State.TURN_OF_HUMAN)return
        reversePiece(coordinate)
    }

    private fun reversePiece(coordinate: Coordinate) {
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
        changeState(State.JUDGE_FIRST)
    }

    override fun onInit() {
        board.resetPiece()
        board.resetCellColor()
        changeState(State.START)
    }

    override fun onStart() {
        changeState(State.JUDGE_FIRST)
    }

    override fun onTurnPlayer() {
        when (val player = playerManager.turnPlayer) {
            is CPU -> {
                state = State.TURN_OF_CPU
                player.play(board)
                playerManager.alternateTurnPlayer()
                //セルの色をリセット
                board.resetCellColor()
                changeState(State.JUDGE_FIRST)
            }
            is Human -> {
                state = State.TURN_OF_HUMAN
            }
        }
    }

    override fun onJudgeFirst() {
        //駒が置けるかチェック
        var cells = ReversiLogic.getCellToPutPiece(playerManager.turnPlayer, board)
        if (cells.isEmpty()) {
            changeState(State.JUDGE_SECOND)
        } else {
            cellsToPutPiece = cells
            paintCellsToPuPiece(cells)
            playerName.onNext(playerManager.turnPlayer.name)
            changeState(State.PLAYER)
        }
    }

    override fun onJudgeSecond() {
        //ターンプレイヤーを変更
        playerManager.alternateTurnPlayer()
        val cells = ReversiLogic.getCellToPutPiece(playerManager.turnPlayer, board)
        if (cells.isEmpty()) {
            winnerPlayerName.onNext(
                ReversiLogic.getWinnerName(board, playerManager.players)
            )
            changeState(State.FINISH)
        } else {
            cellsToPutPiece = cells
            paintCellsToPuPiece(cells)
            playerName.onNext(playerManager.turnPlayer.name)
            changeState(State.PLAYER)
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
