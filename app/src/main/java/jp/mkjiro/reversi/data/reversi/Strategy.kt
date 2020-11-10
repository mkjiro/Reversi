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

class AlphaBetaStrategy : Strategy {
    val indexTable = arrayOf(
        arrayOf(120, -20, 20, 5, 5, 20, -20, 120),
        arrayOf(-20, -40, -5, -5, -5, -5, -40, -20),
        arrayOf(20, -5, 15, 3, 3, 15, -5, 20),
        arrayOf(5, -5, 3, 3, 3, 3, -5, 5),
        arrayOf(5, -5, 3, 3, 3, 3, -5, 5),
        arrayOf(20, -5, 15, 3, 3, 15, -5, 20),
        arrayOf(-20, -40, -5, -5, -5, -5, -40, -20),
        arrayOf(120, -20, 20, 5, 5, 20, -20, 120)
    )

    override fun getChosen(player: Player, board: Board): Coordinate {

        return Coordinate()
    }
}