package jp.mkjiro.reversi.domain.reversi

import jp.mkjiro.reversi.data.reversi.*
import javax.inject.Inject
import javax.inject.Singleton

interface ReversiFactory {
    fun create(columns: Int, rows: Int): Reversi
}

@Singleton
class ReversiFactoryImpl @Inject constructor(
) : ReversiFactory {

    override fun create(
        columns: Int,
        rows: Int
    ): Reversi {
        return Reversi(
            Board(columns, rows),
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
    }
}