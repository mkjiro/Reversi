package jp.mkjiro.reversi.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jp.mkjiro.reversi.R
import kotlinx.android.synthetic.main.cell.view.*

class BoardRecyclerAdapter(
    private val list : List<List<Int>>
): RecyclerView.Adapter<BoardRecyclerAdapter.MyViewHolder>(){

    // リスナー格納変数
    private lateinit var listener: OnItemClickListener
    private val listColumns : Int get() = list[0].size
    private val listRows : Int get() = list.size

    // Viewの初期化
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val text:TextView = view.piece_textView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val row = position/listColumns
        val col = position%listColumns
        holder.text.text = list[row][col].toString()

        holder.text.setOnClickListener {
            listener.onItemClickListener(it, position, list[row][col].toString())
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = listColumns * listRows

    //インターフェースの作成
    interface OnItemClickListener{
        fun onItemClickListener(view: View, position: Int, clickedText: String)
    }

    // リスナー
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

}