package jp.mkjiro.reversi.usecase.reversi

import jp.mkjiro.reversi.domain.reversi.board.Board
import jp.mkjiro.reversi.domain.reversi.board.Piece
import jp.mkjiro.reversi.domain.reversi.board.PieceColor
import jp.mkjiro.reversi.domain.reversi.player.*
import javax.inject.Inject
import javax.inject.Singleton

interface ReversiFactory {
    fun create(columns: Int, rows: Int): Reversi
    fun createHumVSCPU(columns: Int, rows: Int): Reversi
    fun createRanVSRan(columns: Int, rows: Int): Reversi
    fun createAlphaVSAlpha(columns: Int, rows: Int): Reversi
    fun createAlphaVSRan(columns: Int, rows: Int): Reversi
}

@Singleton
class ReversiFactoryImpl @Inject constructor(
) : ReversiFactory {

    override fun create(
        columns: Int,
        rows: Int
    ): Reversi {
        var playerManager =
            PlayerManager(
                arrayOf(
                    Human(
                        "Black",
                        Piece(
                            PieceColor.BLACK
                        )
                    ),
                    Human(
                        "White",
                        Piece(
                            PieceColor.WHITE
                        )
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
        var playerManager =
            PlayerManager(
                arrayOf(
                    Human(
                        "Black",
                        Piece(
                            PieceColor.BLACK
                        )
                    ),
                    CPU(
                        "White",
                        Piece(
                            PieceColor.WHITE
                        ),
//                    RandomStrategy()
                        AlphaBetaStrategy()
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
        var playerManager =
            PlayerManager(
                arrayOf(
                    CPU(
                        "Black",
                        Piece(
                            PieceColor.BLACK
                        ),
                        RandomStrategy()
                    ),
                    CPU(
                        "White",
                        Piece(
                            PieceColor.WHITE
                        ),
                        RandomStrategy()
                    )
                )
            )
        return Reversi(
            Board(columns, rows),
            playerManager
        )
    }

    override fun createAlphaVSAlpha(
        columns: Int,
        rows: Int
    ): Reversi {
        var playerManager =
            PlayerManager(
                arrayOf(
                    CPU(
                        "Black",
                        Piece(
                            PieceColor.BLACK
                        ),
                        AlphaBetaStrategy()
                    ),
                    CPU(
                        "White",
                        Piece(
                            PieceColor.WHITE
                        ),
                        AlphaBetaStrategy()
                    )
                )
            )
        return Reversi(
            Board(columns, rows),
            playerManager
        )
    }

    override fun createAlphaVSRan(
        columns: Int,
        rows: Int
    ): Reversi {
        var playerManager =
            PlayerManager(
                arrayOf(
                    CPU(
                        "Black",
                        Piece(
                            PieceColor.BLACK
                        ),
                        AlphaBetaStrategy()
                    ),
                    CPU(
                        "White",
                        Piece(
                            PieceColor.WHITE
                        ),
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