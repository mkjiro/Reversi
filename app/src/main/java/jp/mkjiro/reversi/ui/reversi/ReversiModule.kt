package jp.mkjiro.reversi.ui.reversi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jp.mkjiro.reversi.di.ByFactory
import jp.mkjiro.reversi.di.ViewModelKey
import jp.mkjiro.reversi.di.get
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class ReversiModule {

    @Binds
    @IntoMap
    @ViewModelKey(ReversiViewModel::class)
    abstract fun bindsNextViewModel(viewModel: ReversiViewModel): ViewModel


    @Module
    companion object {

        @JvmStatic
        @Provides
        @ByFactory
        fun providesNextViewModel(
            fragment: ReversiFragment,
            viewModelFactory: ViewModelProvider.Factory
        ): ReversiViewModel = viewModelFactory.get(fragment)
    }
}
