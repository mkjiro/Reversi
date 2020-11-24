package jp.mkjiro.reversi.domain.reversi.player

import jp.mkjiro.reversi.domain.reversi.board.Board
import jp.mkjiro.reversi.domain.reversi.board.Piece

class Human(
    name: String,
    piece: Piece
) : Player(name, piece) {

    override fun play(playerManager: PlayerManager, board: Board) {
        state = State.READY
    }

    fun isReady(): Boolean { return state == State.READY }
}