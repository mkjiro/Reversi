package jp.mkjiro.reversi.domain.reversi

import jp.mkjiro.reversi.data.reversi.*
import javax.inject.Inject
import javax.inject.Singleton

interface ReversiFactory {
    fun create(columns: Int, rows: Int): Reversi
    fun createHumVSCPU(columns: Int, rows: Int): Reversi
    fun createRanVSRan(columns: Int, rows: Int): Reversi
}

@Singleton
class ReversiFactoryImpl @Inject constructor(
) : ReversiFactory {

    override fun create(
        columns: Int,
        rows: Int
    ): Reversi {
        var playerManager = PlayerManager(
            arrayOf(
                Human(
                    "Black",
                    Piece(PieceColor.BLACK)
                ),
                Human(
                    "White",
                    Piece(PieceColor.WHITE)
                )
            )
        )
        return Reversi(
            Board(columns, rows),
            playerManager
        )
    }

    override fun createHumVSCPU(
        columns: Int,
        rows: Int
    ): Reversi {
        var playerManager = PlayerManager(
            arrayOf(
                Human(
                    "Black",
                    Piece(PieceColor.BLACK)
                ),
                CPU(
                    "White",
                    Piece(PieceColor.WHITE),
                    RandomStrategy()
                )
            )
        )
        return Reversi(
            Board(columns, rows),
            playerManager
        )
    }

    override fun createRanVSRan(
        columns: Int,
        rows: Int
    ): Reversi {
        var playerManager = PlayerManager(
            arrayOf(
                CPU(
                    "Black",
                    Piece(PieceColor.BLACK),
                    RandomStrategy()
                ),
                CPU(
                    "White",
                    Piece(PieceColor.WHITE),
                    RandomStrategy()
                )
            )
        )
        return Reversi(
            Board(columns, rows),
            playerManager
        )
    }
}