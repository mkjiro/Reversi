package jp.mkjiro.reversi.domain.reversi

import dagger.Binds
import dagger.Module

@Module
abstract class ReversiServiceModule{
    @Binds
    abstract fun bindsReversi(impl:ReversiFactoryImpl):ReversiFactory
}