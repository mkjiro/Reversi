package jp.mkjiro.reversi.domain.reversi

import javax.inject.Inject
import javax.inject.Singleton

interface ReversiRepository {
    fun create(columns: Int, rows: Int): Reversi
    fun createHumVSCPU(columns: Int, rows: Int): Reversi
    fun createRanVSRan(columns: Int, rows: Int): Reversi
    fun createAlphaVSAlpha(columns: Int, rows: Int): Reversi
    fun createAlphaVSRan(columns: Int, rows: Int): Reversi
}

@Singleton
class ReversiRepositoryImpl @Inject constructor(
    private val reversiFactory: ReversiFactory
) : ReversiRepository {
    override fun create(columns: Int, rows: Int): Reversi {
        return reversiFactory.create(columns, rows)
    }

    override fun createHumVSCPU(columns: Int, rows: Int): Reversi {
        return reversiFactory.createHumVSCPU(columns, rows)
    }

    override fun createRanVSRan(columns: Int, rows: Int): Reversi {
        return reversiFactory.createRanVSRan(columns, rows)
    }

    override fun createAlphaVSAlpha(columns: Int, rows: Int): Reversi {
        return reversiFactory.createAlphaVSAlpha(columns, rows)
    }

    override fun createAlphaVSRan(columns: Int, rows: Int): Reversi {
        return reversiFactory.createAlphaVSRan(columns, rows)
    }
}
