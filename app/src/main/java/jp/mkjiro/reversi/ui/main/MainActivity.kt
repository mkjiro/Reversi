package jp.mkjiro.reversi.ui.main

import android.os.*
import androidx.navigation.NavController
import androidx.navigation.findNavController
import jp.mkjiro.reversi.BuildConfig
import jp.mkjiro.reversi.R
//import com.google.firebase.perf.FirebasePerformance
import jp.mkjiro.reversi.base.BaseActivity
import jp.mkjiro.reversi.databinding.ActivityMainBinding
import jp.mkjiro.reversi.ui.livedata.EventLiveDataObserver
import jp.mkjiro.reversi.di.ByFactory
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    @field:ByFactory
    lateinit var viewModel: MainViewModel

    lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.w("onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // start tracing performance
        init()
//        FirebasePerformance.getInstance().newTrace("main_trace").start()
    }

    private fun init() {
        Locale.setDefault(Locale.JAPANESE)
        lifecycle.run {
            addObserver(viewModel.disposableObserver)
        }
        initNavigationEvent()
        configureNavController()

        binding.textVersion.text = BuildConfig.VERSION_NAME
    }

    private fun initNavigationEvent() {
        viewModel.liveEvent.observe(
            this,
            EventLiveDataObserver(
                this::onMainNavigationEventReceive
            )
        )
    }

    private fun onMainNavigationEventReceive(event: MainEvents) {
    }

    private fun configureNavController() {
        navController = findNavController(R.id.nav_controller)
        navController.addOnDestinationChangedListener { _, destination, _ ->
        }
    }

    override fun onDestroy() {
        Timber.w("onDestroy()")
//        FirebasePerformance.getInstance().newTrace("main_trace").stop()
        super.onDestroy()
    }
}