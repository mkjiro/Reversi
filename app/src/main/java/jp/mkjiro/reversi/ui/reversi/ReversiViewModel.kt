package jp.mkjiro.reversi.ui.reversi

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import jp.mkjiro.reversi.base.BaseViewModel
import jp.mkjiro.reversi.data.reversi.Coordinate
import jp.mkjiro.reversi.domain.reversi.ReversiRepository
import jp.mkjiro.reversi.ui.livedata.EventLiveData
import javax.inject.Inject

class ReversiViewModel @Inject constructor(
    reversiRepository: ReversiRepository
) : BaseViewModel<ReversiEvents>() {
    override val liveEvent =
        EventLiveData<ReversiEvents>()

    val reversi = reversiRepository.createVSCPU(8, 8)

    val rows: Int
        get() = reversi.getBoard().cells.size
    val columns: Int
        get() = reversi.getBoard().cells[0].size

    val turnPlayerName: PublishProcessor<String> by lazy {
        PublishProcessor.create<String>()
    }

    val winnerPlayerName: PublishProcessor<String> by lazy {
        PublishProcessor.create<String>()
    }

    override fun onStartWithDisposables(disposables: CompositeDisposable) {
        super.onStartWithDisposables(disposables)
        reversi.getTurnPlayerName().subscribe {
            val shownName = "$it's Turn"
            turnPlayerName.onNext(shownName)
        }.let(disposables::add)

        reversi.getWinnerName().subscribe {
            val showName = "$it is Winner !!!"
            winnerPlayerName.onNext(showName)
        }.let(disposables::add)
    }

    fun putPiece(position: Int) {
        var coordinate = Coordinate(position / columns, position % columns)
        reversi.putPiece(coordinate)
    }
}
