package jp.mkjiro.reversi.ui.reversi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import jp.mkjiro.reversi.R
import jp.mkjiro.reversi.base.BaseFragment
import jp.mkjiro.reversi.databinding.FragmentReversiBinding
import kotlinx.android.synthetic.main.fragment_reversi.*

class ReversiFragment : BaseFragment<ReversiEvents, ReversiViewModel>() {

    private lateinit var binding: FragmentReversiBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReversiBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_button.setOnClickListener {
            viewModel.liveEvent.emitEvent(ReversiEvents.ToHome)
        }
    }

    override fun onLiveEventReceive(event: ReversiEvents) {
        when(event){
            is ReversiEvents.ToHome -> {
                findNavController().navigate(R.id.home)
            }
        }
    }
}
