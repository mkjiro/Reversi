package jp.mkjiro.reversi.view

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import jp.mkjiro.reversi.R
import jp.mkjiro.reversi.domain.reversi.board.Board
import jp.mkjiro.reversi.domain.reversi.board.CellColor
import jp.mkjiro.reversi.domain.reversi.board.PieceColor
import kotlinx.android.synthetic.main.cell.view.*

class BoardRecyclerAdapter(
    private var board: Board,
    private var resources: Resources
) : RecyclerView.Adapter<BoardRecyclerAdapter.MyViewHolder>() {

    // リスナー格納変数
    private lateinit var listener: OnItemClickListener
    private val arrayColumns: Int get() = board.cells[0].size
    private val arrayRows: Int get() = board.cells.size

    private val white = R.drawable.piece_white_style
    private val black = R.drawable.piece_black_style
    private val green = R.drawable.piece_style

    private val cellColorGreen = R.drawable.cell_style_green
    private val cellColorRed = R.drawable.cell_style_red

    // Viewの初期化
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val piece: TextView = view.piece_textView
        val cell: TextView = view.cell_textView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val row = position / arrayColumns
        val col = position % arrayColumns

//        holder.cell.text = "$row : $col"

        holder.cell.setOnClickListener {
            listener.onItemClickListener(it, position, board.cells[row][col].toString())
        }
        //        holder.text.text = board.cells[row][col].toString()
        board.cells[row][col].piece?.let {
            holder.piece.visibility = View.VISIBLE
            holder.piece.background = when (board.cells[row][col].piece!!.color) {
                PieceColor.WHITE -> ResourcesCompat.getDrawable(resources, white, null)
                PieceColor.BLACK -> ResourcesCompat.getDrawable(resources, black, null)
            }
        } ?: run {
            holder.piece.visibility = View.GONE
        }

        holder.cell.background = when (board.cells[row][col].color) {
            CellColor.GREEN -> ResourcesCompat.getDrawable(resources, cellColorGreen, null)
            CellColor.RED -> ResourcesCompat.getDrawable(resources, cellColorRed, null)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = arrayColumns * arrayRows

    //インターフェースの作成
    interface OnItemClickListener {
        fun onItemClickListener(view: View, position: Int, clickedText: String)
    }

    // リスナー
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}