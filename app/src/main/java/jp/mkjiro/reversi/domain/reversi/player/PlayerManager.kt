package jp.mkjiro.reversi.reversi.player

class PlayerManager(var players: Array<Player>) {
    private var turnPlayerIndex = 0

    val turnPlayer: Player get() = players[turnPlayerIndex]

    fun alternateTurnPlayer() {
        turnPlayerIndex = (turnPlayerIndex + 1) % players.size
    }

    fun copy(): PlayerManager {
        val playerManager =
            PlayerManager(this.players)
        playerManager.turnPlayerIndex = this.turnPlayerIndex
        return playerManager
    }
}