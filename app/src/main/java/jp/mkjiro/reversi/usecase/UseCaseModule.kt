package jp.mkjiro.reversi.usecase

import dagger.Module
import jp.mkjiro.reversi.usecase.reversi.ReversiServiceModule

@Module(
    includes = [
        ReversiServiceModule::class
    ]
)
class UseCaseModule