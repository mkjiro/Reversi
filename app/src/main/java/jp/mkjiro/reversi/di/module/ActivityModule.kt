package jp.mkjiro.reversi.di.module

import jp.mkjiro.reversi.di.scope.ActivityScope
import jp.mkjiro.reversi.ui.main.MainActivity
import jp.mkjiro.reversi.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            MainActivityModule::class
        ]
    )
    fun contributesMainActivity(): MainActivity
}
