package jp.mkjiro.reversi.data

import jp.mkjiro.reversi.data.api.NetworkModule
import dagger.Module

@Module(
    includes = [
        NetworkModule::class
    ]
)
class DataModule {}