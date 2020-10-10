package jp.mkjiro.reversi.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import jp.mkjiro.reversi.di.ByFactory
import jp.mkjiro.reversi.ui.livedata.EventLiveDataObserver
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment<Nav : BaseEvents, VM : BaseViewModel<Nav>> :
    Fragment(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    @field:ByFactory
    lateinit var viewModel: VM

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any>? {
        return androidInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.run {
            addObserver(viewModel.disposableObserver)
        }
        viewModel.liveEvent
            .observe(
                this,
                EventLiveDataObserver(
                    this::onLiveEventReceive
                )
            )
    }

    abstract fun onLiveEventReceive(event: Nav)
}