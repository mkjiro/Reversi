package jp.mkjiro.reversi.domain.reversi

import jp.mkjiro.reversi.data.reversi.Board
import jp.mkjiro.reversi.data.reversi.Piece
import jp.mkjiro.reversi.data.reversi.PieceColor
import jp.mkjiro.reversi.data.reversi.Player
import javax.inject.Inject
import javax.inject.Singleton

interface ReversiFactory{
    fun create(columns:Int,rows:Int): Reversi
}

@Singleton
class ReversiFactoryImpl @Inject constructor(
):ReversiFactory{

    override fun create(
        columns:Int,
        rows:Int
    ): Reversi {
        return Reversi(
            Board(columns,rows),
            arrayOf(
                Player(
                    "Black",
                    Piece(PieceColor.BLACK)
                ),
                Player(
                    "White",
                    Piece(PieceColor.WHITE)
                )
            )
        )
    }
}