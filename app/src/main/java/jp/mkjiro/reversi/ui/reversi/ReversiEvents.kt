package jp.mkjiro.reversi.ui.reversi

import jp.mkjiro.reversi.base.BaseEvents

sealed class ReversiEvents : BaseEvents {
    object ToHome : ReversiEvents()
}
