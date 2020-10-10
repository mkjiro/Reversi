package jp.mkjiro.reversi.ui.reversi

import jp.mkjiro.reversi.base.BaseViewModel
import jp.mkjiro.reversi.ui.livedata.EventLiveData
import javax.inject.Inject

class ReversiViewModel @Inject constructor(
) : BaseViewModel<ReversiEvents>() {
    override val liveEvent =
        EventLiveData<ReversiEvents>()

}
