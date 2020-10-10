package jp.mkjiro.reversi.common.resource

import android.app.Activity
import androidx.annotation.ArrayRes

class GetStringArray constructor(private val activity: Activity) {
    operator fun invoke(@ArrayRes resId: Int) = activity.resources.getStringArray(resId)
}
