package jp.mkjiro.reversi.ui.livedata

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface LifecycleDisposable : LifecycleObserver {
    val resumeDisposables: CompositeDisposable
    val startDisposables: CompositeDisposable
    val createDisposables: CompositeDisposable
}

interface WithLifecycleDisposing {

    val disposableObserver: LifecycleDisposable

    fun onCreateWithDisposables(disposables: CompositeDisposable) {}

    fun onStartWithDisposables(disposables: CompositeDisposable) {}

    fun onResumeWithDisposables(disposables: CompositeDisposable) {}

    fun onPause() {}

    fun onStop() {}

    fun onDestroy() {}
}

private class LifecycleDisposableImpl(private val target: WithLifecycleDisposing) :
    LifecycleDisposable {

    override val createDisposables = CompositeDisposable()
    override val startDisposables = CompositeDisposable()
    override val resumeDisposables = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        createDisposables.clear()
        target.onCreateWithDisposables(createDisposables)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        startDisposables.clear()
        target.onStartWithDisposables(startDisposables)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        resumeDisposables.clear()
        target.onResumeWithDisposables(resumeDisposables)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        resumeDisposables.clear()
        target.onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        startDisposables.clear()
        target.onStop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        createDisposables.clear()
        target.onDestroy()
    }
}

fun <T> lifecycleDisposable(): ReadOnlyProperty<T, LifecycleDisposable> where T : ViewModel, T : WithLifecycleDisposing =
    object : ReadOnlyProperty<T, LifecycleDisposable> {
        private var lifecycleDisposable: LifecycleDisposable? = null

        override fun getValue(thisRef: T, property: KProperty<*>): LifecycleDisposable =
            lifecycleDisposable
                ?: LifecycleDisposableImpl(
                    thisRef
                ).also { lifecycleDisposable = it }
    }
