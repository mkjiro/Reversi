package jp.mkjiro.reversi.usecase.reversi

import io.reactivex.processors.PublishProcessor
import io.reactivex.subjects.BehaviorSubject
import jp.mkjiro.reversi.domain.reversi.*
import jp.mkjiro.reversi.domain.reversi.board.Board
import jp.mkjiro.reversi.domain.reversi.board.CellColor
import jp.mkjiro.reversi.domain.reversi.board.Coordinate
import jp.mkjiro.reversi.domain.reversi.player.CPU
import jp.mkjiro.reversi.domain.reversi.player.Human
import jp.mkjiro.reversi.domain.reversi.player.PlayerManager
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class Reversi(
    private val board: Board,
    private val playerManager: PlayerManager
) : ReversiStateMachine(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    private val playerName: BehaviorSubject<String> by lazy {
        BehaviorSubject.create<String>()
    }
    private val winnerPlayerName: PublishProcessor<String> by lazy {
        PublishProcessor.create<String>()
    }
    private var cellsToPutPiece: Array<Coordinate> = arrayOf()

    init {
        setFirstState()
    }

    fun start() {
        emitEvent(Event.START)
    }

    fun finish() {
        job.cancel()
    }

    fun putPiece(coordinate: Coordinate) {
        Timber.d("coordinate to put : %s", coordinate)
        Timber.d("%s", state)
        val player = playerManager.turnPlayer
        if (player is Human && player.isReady()) {
            reversePiece(coordinate)
        }
    }

    private fun reversePiece(coordinate: Coordinate) {
        //駒が置ける場所かチェック
        if (!cellsToPutPiece.contains(coordinate))return //置けない場所
        playerManager.turnPlayer.run()
        //ボードに駒を置く
        playerManager.turnPlayer.putPiece(coordinate, board)
        //ひっくり返す
        ReversiLogic.getOverturnedPieces(coordinate, playerManager.turnPlayer, board)
            .map {
                playerManager.turnPlayer.putPiece(it, board)
            }
        playerManager.turnPlayer.finish()
        //ターンプレイヤーを変更
        playerManager.alternateTurnPlayer()
        //セルの色をリセット
        board.resetCellColor()
        emitEvent(Event.PUT)
    }

    override fun runInitPhase() {
        board.resetPiece()
        board.resetCellColor()
    }

    override fun runPlayerPhase() {
        Timber.d("%s", playerManager.turnPlayer)
        when (val player = playerManager.turnPlayer) {
            is CPU -> {
                runBlocking {
                    delay(2000)
                    player.play(playerManager, board)
                    playerManager.alternateTurnPlayer()
                    //セルの色をリセット
                    board.resetCellColor()
                    emitEvent(Event.PUT)
                }
            }
            is Human -> {
                player.play(playerManager, board)
            }
        }
    }

    override fun runJudgeFirstPhase() {
        //駒が置けるかチェック
        var cells = ReversiLogic.getCellToPutPiece(playerManager.turnPlayer, board)
        if (cells.isEmpty()) {
            emitEvent(Event.NOT_PUT)
        } else {
            cellsToPutPiece = cells
            paintCellsToPuPiece(cells)
            playerName.onNext(playerManager.turnPlayer.name)
            emitEvent(Event.CONTINUE)
        }
    }

    override fun runJudgeSecondPhase() {
        //ターンプレイヤーを変更
        playerManager.alternateTurnPlayer()
        val cells = ReversiLogic.getCellToPutPiece(playerManager.turnPlayer, board)
        if (cells.isEmpty()) {
            winnerPlayerName.onNext(
                ReversiLogic.getWinnerName(board, playerManager.players)
            )
            emitEvent(Event.NOT_PUT)
        } else {
            cellsToPutPiece = cells
            paintCellsToPuPiece(cells)
            playerName.onNext(playerManager.turnPlayer.name)
            emitEvent(Event.CONTINUE)
        }
    }

    private fun paintCellsToPuPiece(cells: Array<Coordinate>) {
        if (cells.isNotEmpty()) {
            cells.map {
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
