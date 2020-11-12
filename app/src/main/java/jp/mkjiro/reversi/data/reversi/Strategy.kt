package jp.mkjiro.reversi.data.reversi

import jp.mkjiro.reversi.domain.reversi.ReversiLogic

interface Strategy {
    fun getChosen(playerManager: PlayerManager, board: Board): Coordinate
}

class RandomStrategy : Strategy {
    override fun getChosen(playerManager: PlayerManager, board: Board): Coordinate {
        val cells =
                ReversiLogic.getCellToPutPiece(playerManager.turnPlayer, board)
        return cells.random()
    }
}

class AlphaBetaStrategy : Strategy {
    private val indexTable = arrayOf(
        arrayOf(120, -20, 20, 5, 5, 20, -20, 120),
        arrayOf(-20, -40, -5, -5, -5, -5, -40, -20),
        arrayOf(20, -5, 15, 3, 3, 15, -5, 20),
        arrayOf(5, -5, 3, 3, 3, 3, -5, 5),
        arrayOf(5, -5, 3, 3, 3, 3, -5, 5),
        arrayOf(20, -5, 15, 3, 3, 15, -5, 20),
        arrayOf(-20, -40, -5, -5, -5, -5, -40, -20),
        arrayOf(120, -20, 20, 5, 5, 20, -20, 120)
    )

    private val MAX_DEPTH = 4
    private val MAX_INDEX = -10000
    private val MIN_INDEX = 10000

    private fun reversePiece(coordinate: Coordinate, playerManager: PlayerManager, board: Board) {
        playerManager.turnPlayer.putPiece(coordinate, board)
        ReversiLogic.getOverturnedPieces(coordinate, playerManager.turnPlayer, board)
            .map {
                playerManager.turnPlayer.putPiece(it, board)
            }
    }

    private fun getMinIndex(playerManager: PlayerManager, board: Board, depth: Int): Int {
        var minCoorinate = Coordinate(-1, -1)
        var minIndexSum = MIN_INDEX
        if (depth > MAX_DEPTH)return 0
        ReversiLogic.getCellToPutPiece(
            playerManager.turnPlayer,
            board
        ).map { cd ->
            val pm = playerManager.copy()
            val bd = board.copy()
            reversePiece(cd, pm, bd)
            pm.alternateTurnPlayer()
            val indexSum = - indexTable[cd.y][cd.x] + getMaxIndex(pm, bd, depth + 1)
            if (minIndexSum > indexSum) {
                minIndexSum = indexSum
                minCoorinate = cd
            }
        }
        return if (minCoorinate == Coordinate(-1, -1)) {
            0
        } else {
            minIndexSum
        }
    }

    private fun getMaxIndex(playerManager: PlayerManager, board: Board, depth: Int): Int {
        var maxCoorinate = Coordinate(-1, -1)
        var maxIndexSum = MAX_INDEX
        if (depth > MAX_DEPTH)return 0
        ReversiLogic.getCellToPutPiece(
            playerManager.turnPlayer,
            board
        ).map { cd ->
            val pm = playerManager.copy()
            val bd = board.copy()
            reversePiece(cd, pm, bd)
            pm.alternateTurnPlayer()
            val indexSum = indexTable[cd.y][cd.x] + getMinIndex(pm, bd, depth + 1)
            if (maxIndexSum < indexSum) {
                maxIndexSum = indexSum
                maxCoorinate = cd
            }
        }
        return if (maxCoorinate == Coordinate(-1, -1)) {
            0
        } else {
            maxIndexSum
        }
    }

    override fun getChosen(playerManager: PlayerManager, board: Board): Coordinate {
        var maxCoorinate = Coordinate(-1, -1)
        var maxIndexSum = -1000000
        ReversiLogic.getCellToPutPiece(
            playerManager.turnPlayer,
            board
        ).map { cd ->
            val pm = playerManager.copy()
            val bd = board.copy()
            reversePiece(cd, pm, bd)
            pm.alternateTurnPlayer()
            val indexSum = indexTable[cd.y][cd.x] + getMinIndex(pm, bd, 1)
            if (maxIndexSum < indexSum) {
                maxIndexSum = indexSum
                maxCoorinate = cd
            }
        }
        return maxCoorinate
    }
}