package jp.mkjiro.reversi.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import jp.mkjiro.reversi.App
import jp.mkjiro.reversi.BuildConfig
import jp.mkjiro.reversi.domain.DomainModule
import jp.mkjiro.reversi.usecase.UseCaseModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule

@Module(
    includes = [
        AndroidInjectionModule::class,
        ViewModelModule::class,
        UseCaseModule::class,
        DomainModule::class,
        ActivityModule::class
    ]
)
abstract class AppModule {

    @Binds
    abstract fun provideApplication(app: App): Application

    @Binds
    abstract fun provideApplicationContext(application: Application): Context

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun providesSharedPreferences(
            context: Context
        ): SharedPreferences = context.getSharedPreferences(
            BuildConfig.APPLICATION_ID,
            AppCompatActivity.MODE_PRIVATE
        )
    }
}
