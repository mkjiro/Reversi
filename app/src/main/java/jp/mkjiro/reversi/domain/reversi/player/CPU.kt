package jp.mkjiro.reversi.reversi.player

import jp.mkjiro.reversi.reversi.*
import jp.mkjiro.reversi.reversi.board.Board
import jp.mkjiro.reversi.reversi.board.Piece

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