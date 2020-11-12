package jp.mkjiro.reversi.data.reversi

import jp.mkjiro.reversi.domain.reversi.ReversiLogic

class CPU(
    name: String,
    piece: Piece,
    var strategy: Strategy
) : Player(name, piece) {
    override fun play(playerManager: PlayerManager, board: Board) {
        val coordinate = strategy.getChosen(playerManager, board)
        putPiece(coordinate, board)
        //ひっくり返す
        ReversiLogic.getOverturnedPieces(coordinate, this, board)
            .map {
                this.putPiece(it, board)
            }
    }
}