package jp.mkjiro.reversi.view

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import jp.mkjiro.reversi.R
import jp.mkjiro.reversi.data.reversi.Board
import jp.mkjiro.reversi.data.reversi.Color
import kotlinx.android.synthetic.main.cell.view.*

class BoardRecyclerAdapter(
    private var board : Board,
    private var resources : Resources
): RecyclerView.Adapter<BoardRecyclerAdapter.MyViewHolder>(){

    // リスナー格納変数
    private lateinit var listener: OnItemClickListener
    private val arrayColumns : Int get() = board.cells[0].size
    private val arrayRows : Int get() = board.cells.size

    private val white = R.drawable.piece_white_style
    private val black = R.drawable.piece_black_style
    private val green = R.drawable.piece_style

    // Viewの初期化
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val text:TextView = view.piece_textView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val row = position/arrayColumns
        val col = position%arrayColumns
//        holder.text.text = board.cells[row][col].toString()
        holder.text.background = when(board.cells[row][col].color){
            Color.WHITE -> ResourcesCompat.getDrawable(resources,white,null)
            Color.BLACK -> ResourcesCompat.getDrawable(resources,black,null)
            else -> ResourcesCompat.getDrawable(resources,green,null)
        }

        holder.text.setOnClickListener {
            listener.onItemClickListener(it, position, board.cells[row][col].toString())
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = arrayColumns * arrayRows

    //インターフェースの作成
    interface OnItemClickListener{
        fun onItemClickListener(view: View, position: Int, clickedText: String)
    }

    // リスナー
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

}