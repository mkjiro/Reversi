package jp.mkjiro.reversi.ui.main

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jp.mkjiro.reversi.common.resource.GetString
import jp.mkjiro.reversi.common.resource.GetStringArray
import jp.mkjiro.reversi.di.ByFactory
import jp.mkjiro.reversi.di.ViewModelKey
import jp.mkjiro.reversi.di.get
import jp.mkjiro.reversi.di.scope.FragmentScope
import jp.mkjiro.reversi.ui.home.HomeFragment
import jp.mkjiro.reversi.ui.home.HomeModule
import jp.mkjiro.reversi.ui.reversi.ReversiFragment
import jp.mkjiro.reversi.ui.reversi.ReversiModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MainActivityModule {
    @Binds
    abstract fun bindsAppCompatActivity(activity: MainActivity): AppCompatActivity

    @FragmentScope
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun contributesHomeFragment(): HomeFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [ReversiModule::class])
    abstract fun contributesNextFragment(): ReversiFragment

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindsMainViewModel(viewModel: MainViewModel): ViewModel

    @Module
    companion object {

        @JvmStatic
        @Provides
        @ByFactory
        fun providesMainViewModel(
            activity: AppCompatActivity,
            viewModelFactory: ViewModelProvider.Factory
        ): MainViewModel = viewModelFactory.get(activity)

        @JvmStatic
        @Provides
        fun providesGetString(
            activity: AppCompatActivity
        ): GetString {
            return GetString(activity)
        }

        @JvmStatic
        @Provides
        fun providesGetStringArray(
            activity: AppCompatActivity
        ): GetStringArray {
            return GetStringArray(activity)
        }
    }
}
