package jp.mkjiro.reversi.domain.reversi

import dagger.Binds
import dagger.Module

@Module
abstract class ReversiServiceModule{
    @Binds
    abstract fun bindsReversiFactory(
        impl:ReversiFactoryImpl
    ):ReversiFactory

    @Binds
    abstract fun bindsReversiRepository(
        impl: ReversiRepositoryImpl
    ):ReversiRepository
}