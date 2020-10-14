package jp.mkjiro.reversi.ui.reversi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
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
    private val white = R.drawable.piece_white_style
    private val black = R.drawable.piece_black_style

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

        val layoutManager = GridLayoutManager(context, 8, GridLayoutManager.VERTICAL,false)

        val adapter = BoardRecyclerAdapter(viewModel.array)
        boardRecyclerView.layoutManager = layoutManager
        boardRecyclerView.setHasFixedSize(true)
        boardRecyclerView.adapter = adapter

        //インターフェースの実装
        adapter.setOnItemClickListener(object:BoardRecyclerAdapter.OnItemClickListener{
            override fun onItemClickListener(view: View, position: Int, clickedText: String) {
                view.piece_textView.background = ResourcesCompat.getDrawable(resources,R.drawable.piece_black_style,null)
                Timber.d("%s",view.piece_textView.text)
//                viewModel.test()
                viewModel.array[0][0] = 9999999
                adapter.notifyItemChanged(0)
            }
        })

        back_button.setOnClickListener {
            viewModel.liveEvent.emitEvent(ReversiEvents.ToHome)
        }

        viewModel.reverseLiveData.observe(
            viewLifecycleOwner,
            Observer{
                val turnColor = if(viewModel.isBlack){
                        black
                    }else{
                        white
                    }

                it.forEach {pair ->
                    val h = pair.first
                    val w = pair.second
                    Timber.d("%s %s", h,w)
                    boardRecyclerView.findViewHolderForAdapterPosition(h * 8 + w)?.let {holder ->
                        if(holder is BoardRecyclerAdapter.MyViewHolder){
                            holder.text.piece_textView.background = ResourcesCompat.getDrawable(resources,turnColor,null)
    //                            holder.text.text = "9999999"
                        }
                    }
                }
            }
        )
    }

    override fun onLiveEventReceive(event: ReversiEvents) {
        when(event){
            is ReversiEvents.ToHome -> {
                findNavController().navigate(R.id.home)
            }
        }
    }
}
