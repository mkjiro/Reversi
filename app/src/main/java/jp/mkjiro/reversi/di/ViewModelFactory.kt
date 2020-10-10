package jp.mkjiro.reversi.di

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

private typealias ViewModelCreators = Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>

class ViewModelFactory @Inject constructor(
    private val creators: ViewModelCreators
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass]
            ?: creators.findAssignable(modelClass)
            ?: throw IllegalArgumentException("unknown model class $modelClass")
        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun <T : ViewModel> ViewModelCreators.findAssignable(modelClass: Class<T>): Provider<ViewModel>? =
        entries.firstOrNull { (key, _) -> modelClass.isAssignableFrom(key) }?.value
}

inline fun <reified T : ViewModel> ViewModelProvider.Factory.get(activity: AppCompatActivity): T =
    ViewModelProvider(activity, this).get(T::class.java)

inline fun <reified T : ViewModel> ViewModelProvider.Factory.get(fragment: Fragment): T =
    ViewModelProvider(fragment, this).get(T::class.java)

inline fun <reified T : ViewModel> ViewModelProvider.Factory.get(dialogFragment: DialogFragment): T =
    ViewModelProvider(dialogFragment, this).get(T::class.java)