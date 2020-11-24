package jp.mkjiro.reversi.reversi.player

import jp.mkjiro.reversi.reversi.board.Board
import jp.mkjiro.reversi.reversi.board.Piece

class Human(
    name: String,
    piece: Piece
) : Player(name, piece) {

    override fun play(playerManager: PlayerManager, board: Board) {
        state = State.READY
    }

    fun isReady(): Boolean { return state == State.READY }
}