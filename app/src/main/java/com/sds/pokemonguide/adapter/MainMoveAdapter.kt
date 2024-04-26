package com.sds.pokemonguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sds.pokemonguide.CONSTANTS
import com.sds.pokemonguide.R
import com.sds.pokemonguide.model.Result

class MainMoveAdapter() : PagingDataAdapter<Result, MainMoveAdapter.ViewHolder>(DataDifferntiator) {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.main_move_rv_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val everyMoveName = getItem(position)?.name

        holder.itemView.findViewById<TextView>(R.id.id_move_name).text = everyMoveName!!.replace("-", " ")


        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(getItem(position)!!.url)
            }
        }
    }


    private var onItemClickListenerUrl: ((String) -> Unit)? = null


    fun setOnItemClickListenerUrlMainMove(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }


    object DataDifferntiator : DiffUtil.ItemCallback<Result>() {

        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

}
