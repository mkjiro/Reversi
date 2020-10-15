package jp.mkjiro.reversi.domain

import dagger.Module
import jp.mkjiro.reversi.domain.reversi.ReversiServiceModule

@Module(
    includes = [
        ReversiServiceModule::class
    ]
)
class DomainModule