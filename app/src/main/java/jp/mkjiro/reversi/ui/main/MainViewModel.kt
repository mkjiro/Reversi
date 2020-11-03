package jp.mkjiro.reversi.ui.main

import jp.mkjiro.reversi.base.BaseViewModel
import jp.mkjiro.reversi.ui.livedata.EventLiveData
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainViewModel @Inject constructor(
) : BaseViewModel<MainEvents>() {
    override val liveEvent =
        EventLiveData<MainEvents>()

    override fun onStartWithDisposables(disposables: CompositeDisposable) {
        super.onStartWithDisposables(disposables)
    }

    override fun onStop() {
        super.onStop()
    }
}
