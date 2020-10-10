package jp.mkjiro.reversi.di.module

import androidx.lifecycle.ViewModelProvider
import jp.mkjiro.reversi.di.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
interface ViewModelModule {

    @Binds
    fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
