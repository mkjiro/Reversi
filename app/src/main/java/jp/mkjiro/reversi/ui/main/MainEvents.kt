package jp.mkjiro.reversi.ui.main

import jp.mkjiro.reversi.base.BaseEvents

sealed class MainEvents : BaseEvents {
    object Success : MainEvents()
    object Error : MainEvents()
}
