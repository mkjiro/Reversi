package jp.mkjiro.reversi.data.reversi

import jp.mkjiro.reversi.domain.reversi.ReversiLogic

interface Strategy {
    fun getChosen(player: Player, board: Board): Coordinate
}

class RandomStrategy : Strategy {
    override fun getChosen(player: Player, board: Board): Coordinate {
        val cells = ReversiLogic.getCellToPutPiece(player, board)
        return cells.random()
    }
}