package jp.mkjiro.reversi.domain.reversi.player

import jp.mkjiro.reversi.domain.reversi.*
import jp.mkjiro.reversi.domain.reversi.board.Board
import jp.mkjiro.reversi.domain.reversi.board.Piece

class CPU(
    name: String,
    piece: Piece,
    private var strategy: Strategy
) : Player(name, piece) {
    override fun play(playerManager: PlayerManager, board: Board) {
        state = State.RUNNING
        val coordinate = strategy.getChosen(playerManager, board)
        putPiece(coordinate, board)
        //ひっくり返す
        ReversiLogic.getOverturnedPieces(
            coordinate,
            this,
            board
        )
            .map {
                this.putPiece(it, board)
            }
    }
}