package jp.mkjiro.reversi.ui.reversi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import jp.mkjiro.reversi.base.BaseViewModel
import jp.mkjiro.reversi.ui.livedata.EventLiveData
import timber.log.Timber
import javax.inject.Inject

class ReversiViewModel @Inject constructor(
) : BaseViewModel<ReversiEvents>() {
    override val liveEvent =
        EventLiveData<ReversiEvents>()

    var isBlack = true

    val list = List(8){h ->
        List(8){w ->
            Timber.d("row : %s col: %s", h,w)
            h*8 + w
        }
    }

    private val _reverseLiveData = MutableLiveData<List<Pair<Int,Int>>>()
    val reverseLiveData : LiveData<List<Pair<Int,Int>>>
        get() = _reverseLiveData

    fun test(){
        _reverseLiveData.value = List(4){
            Pair(it,it)
        }
    }
}
