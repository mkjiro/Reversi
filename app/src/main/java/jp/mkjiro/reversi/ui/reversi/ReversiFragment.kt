package jp.mkjiro.reversi.ui.reversi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import io.reactivex.disposables.CompositeDisposable
import jp.mkjiro.reversi.R
import jp.mkjiro.reversi.base.BaseFragment
import jp.mkjiro.reversi.databinding.FragmentReversiBinding
import jp.mkjiro.reversi.view.BoardRecyclerAdapter
import kotlinx.android.synthetic.main.cell.view.*
import kotlinx.android.synthetic.main.fragment_reversi.*

class ReversiFragment : BaseFragment<ReversiEvents, ReversiViewModel>() {

    private lateinit var binding: FragmentReversiBinding

    private val startDisposables = CompositeDisposable()

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
//                view.piece_textView.background = ResourcesCompat.getDrawable(resources,R.drawable.piece_black_style,null)
                viewModel.putPiece(position)
                adapter.notifyDataSetChanged()
            }
        })

        back_button.setOnClickListener {
            viewModel.liveEvent.emitEvent(ReversiEvents.ToHome)
        }

        viewModel.turnPlayerName
            .onBackpressureLatest()
            .subscribe {
                turnPlayerName_text.text = it
            }.let(startDisposables::add)

        viewModel.winnerPlayerName
            .onBackpressureLatest()
            .subscribe {
                turnPlayerName_text.text = it
            }.let(startDisposables::add)
    }

    override fun onPause() {
        startDisposables.clear()
        super.onPause()
    }

    override fun onLiveEventReceive(event: ReversiEvents) {
        when (event) {
            is ReversiEvents.ToHome -> {
                findNavController().navigate(R.id.home)
            }
        }
    }
}
