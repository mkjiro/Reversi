package jp.mkjiro.reversi.base

import androidx.lifecycle.ViewModel
import jp.mkjiro.reversi.ui.livedata.EventLiveData
import jp.mkjiro.reversi.ui.livedata.LifecycleDisposable
import jp.mkjiro.reversi.ui.livedata.WithLifecycleDisposing
import jp.mkjiro.reversi.ui.livedata.lifecycleDisposable
import io.reactivex.Completable
import timber.log.Timber

abstract class BaseViewModel<Nav : BaseEvents>() :
    ViewModel(),
    WithLifecycleDisposing {
    abstract val liveEvent: EventLiveData<Nav>
    final override val disposableObserver: LifecycleDisposable by lifecycleDisposable()
    val resumeDisposables = disposableObserver.resumeDisposables
    val startDisposables = disposableObserver.startDisposables
    val createDisposables = disposableObserver.createDisposables

    protected fun Completable.subscribeWithStartDisposables(
        onComplete: (() -> Unit)? = null,
        onError: (() -> Throwable)? = null
    ) {
        subscribe({
            if (onComplete != null) {
                onComplete.invoke()
            } else {
                Timber.w("subscribeWithStartDisposables onComplete")
            }
        }, {
            if (onError != null) {
                onError.invoke()
            } else {
                Timber.e(it)
            }
        }).let(startDisposables::add)
    }
}
