package jp.mkjiro.reversi.data.reversi

import jp.mkjiro.reversi.domain.reversi.ReversiLogic
import timber.log.Timber

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
        arrayOf(120, -20, 21, 6, 6, 21, -20, 120),
        arrayOf(-20, -40, -5, -5, -5, -5, -40, -20),
        arrayOf(21, -5, 15, 3, 3, 15, -5, 21),
        arrayOf(6, -5, 3, 3, 3, 3, -5, 6),
        arrayOf(6, -5, 3, 3, 3, 3, -5, 6),
        arrayOf(21, -5, 15, 3, 3, 15, -5, 21),
        arrayOf(-20, -40, -5, -5, -5, -5, -40, -20),
        arrayOf(120, -20, 21, 6, 6, 21, -20, 120)
    )

    private val MAX_DEPTH = 5

    private fun reversePiece(coordinate: Coordinate, playerManager: PlayerManager, board: Board) {
        playerManager.turnPlayer.putPiece(coordinate, board)
        ReversiLogic.getOverturnedPieces(coordinate, playerManager.turnPlayer, board)
            .map {
                playerManager.turnPlayer.putPiece(it, board)
            }
    }

    private fun evalUsingDifferPieceSum(pm: PlayerManager, board: Board): Int {
        val myRestPieces = ReversiLogic.getCellToPutPiece(
            pm.turnPlayer,
            board
        ).size
        pm.alternateTurnPlayer()
        val enemyRestPieces = ReversiLogic.getCellToPutPiece(
            pm.turnPlayer,
            board
        ).size
        return myRestPieces - enemyRestPieces
    }

    private fun evalByAlphaBeta(
        coordinate: Coordinate,
        alpha: Int,
        beta: Int,
        playerManager: PlayerManager,
        board: Board,
        depth: Int
    ): Pair<Coordinate, Int> {
        var maxCoordinate = coordinate
        var maxAlpha = alpha
        var maxBeta = beta
        if (depth == 0) {
            val pm = playerManager.copy()
            Timber.d("depth : %s eval : %s", depth, indexTable[coordinate.y][coordinate.x])
            return coordinate to 0
//            return coordinate to indexTable[coordinate.y][coordinate.x]
        }
        ReversiLogic.getCellToPutPiece(
            playerManager.turnPlayer,
            board
        ).map { cd ->
            val pm = playerManager.copy()
            val bd = board.copy()
            reversePiece(cd, pm, bd)
            pm.alternateTurnPlayer()
            var (_, indexSum) = evalByAlphaBeta(cd, -maxBeta, -maxAlpha, pm, bd, depth - 1)
            indexSum += indexTable[cd.y][cd.x]
            if (maxCoordinate == Coordinate(-1, -1))maxCoordinate = cd
            if (maxAlpha <= indexSum) {
                maxAlpha = indexSum
                maxCoordinate = cd
            }
            Timber.d("player: ${playerManager.turnPlayer} depth : $depth ,eval : $indexSum ,alpha : $maxAlpha ,beta : $maxBeta")
            if (maxAlpha >= maxBeta)return maxCoordinate to - maxAlpha
        }
        return maxCoordinate to - maxAlpha
    }

    override fun getChosen(playerManager: PlayerManager, board: Board): Coordinate {
        var maxAlpha = -1000
        var maxBeta = 1000
        Timber.d("===turn start===")
        val pm = playerManager.copy()
        val bd = board.copy()
        val (coordinate, evaluation) = evalByAlphaBeta(Coordinate(-1, -1), maxAlpha, maxBeta, pm, bd, MAX_DEPTH)
        Timber.d("depth : $MAX_DEPTH eval : ${-evaluation}")
        Timber.d("chosen cd : $coordinate")
        Timber.d("===turn end===")
        return coordinate
    }
}