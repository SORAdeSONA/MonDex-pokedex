package com.sds.pokemonguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sds.pokemonguide.R
import com.sds.pokemonguide.model.MovesDetailList

class MoveAilmentAdapter : RecyclerView.Adapter<MoveAilmentAdapter.DataViewHolder>() {

    private var moves: ArrayList<MovesDetailList> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.move_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = moves.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val everyMove = moves[position].name

        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(moves[position].url)
            }
        }

        holder.itemView.findViewById<TextView>(R.id.id_move_name).text = everyMove.replace("-", " ")
    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerUrlMove(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addMoves(movesFromMoveAilmentFragment: List<MovesDetailList>) {
        this.moves.apply {
            clear()
            addAll(movesFromMoveAilmentFragment)
            notifyDataSetChanged()
        }
    }



}