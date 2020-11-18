package jp.mkjiro.reversi.data.reversi

class Human(
    name: String,
    piece: Piece
) : Player(name, piece) {

    override fun play(playerManager: PlayerManager, board: Board) {
        state = State.READY
    }

    fun isReady(): Boolean { return state == State.READY }
}