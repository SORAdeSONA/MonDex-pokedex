package com.sds.pokemonguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sds.pokemonguide.CONSTANTS
import com.sds.pokemonguide.R
import com.sds.pokemonguide.room.RoomBerryModel
import com.sds.pokemonguide.room.RoomMoveModel

class SavedMovesAdapter : RecyclerView.Adapter<SavedMovesAdapter.DataViewHolder>() {

    private var savedMoves: ArrayList<RoomMoveModel> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.main_move_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = savedMoves.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val moveName = savedMoves[position].moveName

        holder.itemView.findViewById<TextView>(R.id.id_move_name).text = moveName.replace("-", " ")

        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(savedMoves[position].moveUrl)
            }
        }
    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerUrl(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addMoves(movesFromDb: List<RoomMoveModel>) {
        this.savedMoves.apply {
            clear()
            addAll(movesFromDb)
            notifyDataSetChanged()
        }
    }



}