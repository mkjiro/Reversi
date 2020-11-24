package jp.mkjiro.reversi.domain.reversi

import jp.mkjiro.reversi.domain.reversi.board.Board
import jp.mkjiro.reversi.domain.reversi.board.Piece
import jp.mkjiro.reversi.domain.reversi.board.PieceColor
import jp.mkjiro.reversi.domain.reversi.player.Human
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import timber.log.Timber

internal class ReversiLogicTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getCellToPutPiece() {
        val player = Human(
            "Black",
            Piece(
                PieceColor.BLACK
            )
        )
        val board = Board()
        board.resetCellColor()
        board.resetPiece()
        val pieces = ReversiLogic.getCellToPutPiece(player, board)
        Timber.d("%s", pieces.size)
        assertTrue(pieces.isNotEmpty())
    }

    @Test
    fun getOverturnedPieces() {
    }

    @Test
    fun getWinnerName() {
    }
}