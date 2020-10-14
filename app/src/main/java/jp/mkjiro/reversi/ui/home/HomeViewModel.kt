package jp.mkjiro.reversi.ui.home

import jp.mkjiro.reversi.BuildConfig
import jp.mkjiro.reversi.base.BaseViewModel
import jp.mkjiro.reversi.ui.livedata.EventLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject constructor(
) : BaseViewModel<HomeEvents>() {
    override val liveEvent =
        EventLiveData<HomeEvents>()

}
