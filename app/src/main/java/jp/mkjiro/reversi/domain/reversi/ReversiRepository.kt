package jp.mkjiro.reversi.domain.reversi

import javax.inject.Inject
import javax.inject.Singleton

interface ReversiRepository{
    fun create(columns:Int,rows:Int):Reversi
}

@Singleton
class ReversiRepositoryImpl @Inject constructor(
    private val reversiFactory: ReversiFactory
):ReversiRepository{
    override fun create(columns:Int,rows:Int): Reversi {
        return reversiFactory.create(columns,rows)
    }
}
