package jp.mkjiro.reversi.ui.home

import jp.mkjiro.reversi.base.BaseViewModel
import jp.mkjiro.reversi.ui.livedata.EventLiveData
import javax.inject.Inject

class HomeViewModel @Inject constructor(
) : BaseViewModel<HomeEvents>() {
    override val liveEvent =
        EventLiveData<HomeEvents>()
}
