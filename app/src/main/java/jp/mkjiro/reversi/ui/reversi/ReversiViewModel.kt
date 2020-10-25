package jp.mkjiro.reversi.ui.reversi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import jp.mkjiro.reversi.base.BaseViewModel
import jp.mkjiro.reversi.data.reversi.Coordinate
import jp.mkjiro.reversi.domain.reversi.Reversi
import jp.mkjiro.reversi.ui.livedata.EventLiveData
import timber.log.Timber
import javax.inject.Inject

class ReversiViewModel @Inject constructor(
    var reversi:Reversi
) : BaseViewModel<ReversiEvents>() {
    override val liveEvent =
        EventLiveData<ReversiEvents>()

    var isBlack = true

    private val rows:Int
        get() = reversi.getBoard().cells.size
    private val columns:Int
        get() = reversi.getBoard().cells[0].size

    val turnPlayerName : PublishProcessor<String> by lazy{
        PublishProcessor.create<String>()
    }

    private val _reverseLiveData = MutableLiveData<Array<Pair<Int,Int>>>()
    val reverseLiveData : LiveData<Array<Pair<Int,Int>>>
        get() = _reverseLiveData

    override fun onStartWithDisposables(disposables: CompositeDisposable) {
        super.onStartWithDisposables(disposables)
        reversi.getTurnPlayerName().subscribe{
            val shownName = "$it's Turn"
            turnPlayerName.onNext(shownName)
        }.let(disposables::add)
    }

    fun test(){
        _reverseLiveData.value = Array(4){
            Pair(it,it)
        }
    }

    fun putPiece(position: Int){
        var coordinate = Coordinate(position/rows,position%columns)
        reversi.putPiece(coordinate)
    }
}
