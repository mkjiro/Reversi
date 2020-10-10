package jp.mkjiro.reversi.ui.livedata

import androidx.lifecycle.Observer

class EventLiveDataObserver<T : Any>(
        private val onEventUnhandledContent: (T) -> Unit
) : Observer<Event<T>> {

    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}
