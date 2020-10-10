package jp.mkjiro.reversi.ui.home

import jp.mkjiro.reversi.base.BaseEvents

sealed class HomeEvents : BaseEvents {
    object ToNext : HomeEvents()
}
