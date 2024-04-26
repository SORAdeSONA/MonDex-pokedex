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
import com.sds.pokemonguide.model.CallbackList
import com.sds.pokemonguide.model.Result

class MainItemAdapter() : PagingDataAdapter<Result, MainItemAdapter.ViewHolder>(DataDifferntiator) {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.main_item_rv_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val everyItemName = getItem(position)?.name

        holder.itemView.findViewById<TextView>(R.id.id_item_name).text = everyItemName!!.replace("-", " ")

        Glide.with(holder.itemView.findViewById<ImageView>(R.id.id_item_image).context)
            .load(CONSTANTS.getItemSpritesByName(everyItemName))
            .into(holder.itemView.findViewById<ImageView>(R.id.id_item_image))


        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
               it(getItem(position)!!.url)
            }
        }
    }


    private var onItemClickListenerUrl: ((String) -> Unit)? = null


    fun setOnItemClickListenerUrlMainItem(listener: (String) -> Unit) {
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
