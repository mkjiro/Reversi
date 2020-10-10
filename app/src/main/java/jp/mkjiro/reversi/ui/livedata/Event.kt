package jp.mkjiro.reversi.ui.livedata

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
class Event<out T : Any>(private val content: T) {

    private var hasBeenHandled: Boolean = false

    /**
     * @return the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? =
            if (hasBeenHandled) {
                null
            } else {
                hasBeenHandled = true
                content
            }

    /**
     * @return the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}
