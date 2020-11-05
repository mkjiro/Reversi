package jp.mkjiro.reversi.data.reversi

class PlayerManager(var players: Array<Player>) {
    private var turnPlayerIndex = 0

    val turnPlayer: Player get() = players[turnPlayerIndex]

    fun alternateTurnPlayer() {
        turnPlayerIndex = (turnPlayerIndex + 1) % players.size
    }
}