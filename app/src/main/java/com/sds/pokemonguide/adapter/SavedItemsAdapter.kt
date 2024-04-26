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
import com.sds.pokemonguide.room.RoomItemModel
import com.sds.pokemonguide.room.RoomPokemonModel

class SavedItemsAdapter : RecyclerView.Adapter<SavedItemsAdapter.DataViewHolder>() {

    private var savedItems: ArrayList<RoomItemModel> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.main_item_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = savedItems.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val itemName = savedItems[position].itemName

        Glide.with(holder.itemView.findViewById<ImageView>(R.id.id_item_image).context)
            .load(CONSTANTS.getItemSpritesByName(itemName))
            .into(holder.itemView.findViewById<ImageView>(R.id.id_item_image))

        holder.itemView.findViewById<TextView>(R.id.id_item_name).text = itemName.replace("-", " ")

        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(savedItems[position].itemUrl)
            }
        }
    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerUrl(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addItems(itemsFromDb: List<RoomItemModel>) {
        this.savedItems.apply {
            clear()
            addAll(itemsFromDb)
            notifyDataSetChanged()
        }
    }



}