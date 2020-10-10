package jp.mkjiro.reversi.ui.livedata

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 * <p>
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue().
 * <p>
 * Note that only one observer is going to be notified of changes.
 */
class EventLiveData<T : Any> : MutableLiveData<Event<T>>() {

    private val isPending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in Event<T>>) {
        if (hasActiveObservers()) {
            throw IllegalStateException("Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer {
            if (isPending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    @MainThread
    override fun setValue(value: Event<T>?) {
        isPending.set(true)
        super.setValue(value)
    }

    fun emitEvent(value: T) {
        postValue(Event(value))
    }
}
