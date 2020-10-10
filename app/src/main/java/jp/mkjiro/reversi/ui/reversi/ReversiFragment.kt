package jp.mkjiro.reversi.ui.reversi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import jp.mkjiro.reversi.R
import jp.mkjiro.reversi.base.BaseFragment
import jp.mkjiro.reversi.databinding.FragmentReversiBinding
import jp.mkjiro.reversi.view.BoardRecyclerAdapter
import kotlinx.android.synthetic.main.cell.view.*
import kotlinx.android.synthetic.main.fragment_reversi.*
import timber.log.Timber

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

        val list = List(8){h ->
            List(8){w ->
                Timber.d("row : %s col: %s", h,w)
                h*8 + w
            }
        }
        val adapter = BoardRecyclerAdapter(list)
        val layoutManager = GridLayoutManager(context, 8, GridLayoutManager.VERTICAL,false)

        boardRecyclerView.layoutManager = layoutManager
        boardRecyclerView.setHasFixedSize(true)
        boardRecyclerView.adapter = adapter

        //インターフェースの実装
        adapter.setOnItemClickListener(object:BoardRecyclerAdapter.OnItemClickListener{
            override fun onItemClickListener(view: View, position: Int, clickedText: String) {
                view.piece_textView.background = ResourcesCompat.getDrawable(resources,R.drawable.piece_black_style,null)
                Timber.d("%s",view.piece_textView.text)
            }
        })

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
