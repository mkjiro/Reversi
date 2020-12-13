package jp.mkjiro.reversi.usecase.reversi

import jp.mkjiro.reversi.domain.reversi.*
import jp.mkjiro.reversi.domain.reversi.board.Board
import jp.mkjiro.reversi.domain.reversi.board.CellColor
import jp.mkjiro.reversi.domain.reversi.board.Coordinate
import jp.mkjiro.reversi.domain.reversi.player.CPU
import jp.mkjiro.reversi.domain.reversi.player.Human
import jp.mkjiro.reversi.domain.reversi.player.PlayerManager
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.consumeAsFlow
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class Reversi(
    private val board: Board,
    private val playerManager: PlayerManager
) : ReversiStateMachine(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    private val _turnPlayerName = Channel<String>(capacity = UNLIMITED)
    val turnPlayerName = _turnPlayerName.consumeAsFlow()

    private val _winnerPlayerName = Channel<String>(capacity = UNLIMITED)
    val winnerPlayerName = _winnerPlayerName.consumeAsFlow()

    private var cellsToPutPiece: Array<Coordinate> = arrayOf()

    init {
        setFirstState()
    }

    suspend fun start() {
        _turnPlayerName.send(playerManager.turnPlayer.name)
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
            launch {
                _turnPlayerName.send(playerManager.turnPlayer.name)
            }
            emitEvent(Event.CONTINUE)
        }
    }

    override fun runJudgeSecondPhase() {
        //ターンプレイヤーを変更
        playerManager.alternateTurnPlayer()
        val cells = ReversiLogic.getCellToPutPiece(playerManager.turnPlayer, board)
        if (cells.isEmpty()) {
            launch {
                _winnerPlayerName.send(
                    ReversiLogic.getWinnerName(board, playerManager.players)
                )
            }
            emitEvent(Event.NOT_PUT)
        } else {
            cellsToPutPiece = cells
            paintCellsToPuPiece(cells)
            launch {
                _turnPlayerName.send(playerManager.turnPlayer.name)
            }
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

    fun getBoard(): Board {
        return board
    }
}
