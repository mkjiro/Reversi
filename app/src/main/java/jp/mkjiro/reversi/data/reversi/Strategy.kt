package jp.mkjiro.reversi.data.reversi

import jp.mkjiro.reversi.domain.reversi.ReversiLogic
import java.lang.Thread.sleep

interface Strategy {
    fun getChosen(player: Player, board: Board): Coordinate
    fun delay(ms: Long)
}

class RandomStrategy : Strategy {
    override fun getChosen(player: Player, board: Board): Coordinate {
        val cells = ReversiLogic.getCellToPutPiece(player, board)
        return cells.random()
    }

    override fun delay(ms: Long) {
        sleep(ms)
    }
}