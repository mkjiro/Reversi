package jp.mkjiro.reversi.ui.home

import jp.mkjiro.reversi.BuildConfig
import jp.mkjiro.reversi.base.BaseViewModel
import jp.mkjiro.reversi.data.api.SampleAPIService
import jp.mkjiro.reversi.ui.livedata.EventLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    val sampleAPIService: SampleAPIService
) : BaseViewModel<HomeEvents>() {
    override val liveEvent =
        EventLiveData<HomeEvents>()

    override fun onStartWithDisposables(disposables: CompositeDisposable) {
        super.onStartWithDisposables(disposables)


        val path = BuildConfig.GOOGLE_PATH
        Timber.d("%s",path)
        val response = sampleAPIService
            .getSpreadSheet(path)

        response
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                Timber.d("HttpStatus : %s",it.code())
                val jsonData = it.body()
                jsonData?.forEach {
                    Timber.d("%s", it.toString())
                }
            },{
                if(it is HttpException){
                    Timber.d("HttpStatus : %s",it.code())
                    Timber.d("Error Message : %s", it.message())
                }
                Timber.e(it)
            }).let(disposables::add)
    }

}
