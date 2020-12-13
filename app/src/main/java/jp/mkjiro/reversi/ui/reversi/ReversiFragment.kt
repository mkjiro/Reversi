package jp.mkjiro.reversi.ui.reversi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import jp.mkjiro.reversi.R
import jp.mkjiro.reversi.base.BaseFragment
import jp.mkjiro.reversi.databinding.FragmentReversiBinding
import jp.mkjiro.reversi.view.BoardRecyclerAdapter
import kotlinx.android.synthetic.main.cell.view.*
import kotlinx.android.synthetic.main.fragment_reversi.*
import kotlinx.coroutines.flow.collect

class ReversiFragment : BaseFragment<ReversiEvents, ReversiViewModel>() {

    private lateinit var binding: FragmentReversiBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReversiBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = GridLayoutManager(context, viewModel.columns, GridLayoutManager.VERTICAL, false)

        val adapter = BoardRecyclerAdapter(viewModel.reversi.getBoard(), resources)
        boardRecyclerView.layoutManager = layoutManager
        boardRecyclerView.setHasFixedSize(true)
        boardRecyclerView.adapter = adapter

        //インターフェースの実装
        adapter.setOnItemClickListener(object : BoardRecyclerAdapter.OnItemClickListener {
            override fun onItemClickListener(view: View, position: Int, clickedText: String) {
                viewModel.putPiece(position)
            }
        })

        back_button.setOnClickListener {
            viewModel.liveEvent.emitEvent(ReversiEvents.ToHome)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.turnPlayerName.collect {
                turnPlayerName_text.text = it
                adapter.notifyDataSetChanged()
            }

            viewModel.winnerPlayerName.collect {
                turnPlayerName_text.text = it
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.setup()
    }

    override fun onStop() {
        viewModel.finish()
        super.onStop()
    }

    override fun onLiveEventReceive(event: ReversiEvents) {
        when (event) {
            is ReversiEvents.ToHome -> {
                findNavController().navigate(R.id.home)
            }
        }
    }
}
