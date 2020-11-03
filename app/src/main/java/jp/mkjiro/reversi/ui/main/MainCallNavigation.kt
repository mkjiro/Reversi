package jp.mkjiro.reversi.ui.main

import android.os.Bundle
import jp.mkjiro.reversi.base.BaseEvents

sealed class MainCallNavigation : BaseEvents {
    object FinishActivity : MainCallNavigation()
    object ToHome : MainCallNavigation()
    object CheckPermissions : MainCallNavigation()
    object CallRequest : MainCallNavigation()
    data class AcceptCall(val bundle: Bundle) : MainCallNavigation()
}
