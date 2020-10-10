package jp.mkjiro.reversi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import jp.mkjiro.reversi.R
import jp.mkjiro.reversi.base.BaseFragment
import jp.mkjiro.reversi.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeEvents, HomeViewModel>() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        move_next_button.setOnClickListener {
            viewModel.liveEvent.emitEvent(HomeEvents.ToNext)
        }
    }

    override fun onLiveEventReceive(event: HomeEvents) {
        when (event) {
            is HomeEvents.ToNext -> {
                findNavController().navigate(R.id.next)
            }
        }
    }
}
