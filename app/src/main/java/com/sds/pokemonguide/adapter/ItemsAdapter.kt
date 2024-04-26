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
import com.sds.pokemonguide.model.ItemsList

class ItemsAdapter() : RecyclerView.Adapter<ItemsAdapter.DataViewHolder>() {

    private var items: ArrayList<ItemsList> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val everyItemName = items[position].item.name

        holder.itemView.findViewById<TextView>(R.id.id_item_name).text = everyItemName.replace("-", " ")

        Glide.with(holder.itemView.findViewById<ImageView>(R.id.id_item_image).context)
            .load(CONSTANTS.getItemSpritesByName(everyItemName))
            .into(holder.itemView.findViewById<ImageView>(R.id.id_item_image))

        holder.itemView.setOnClickListener {
            println(items[position].item.url)
            onItemClickListenerUrl?.let {
                it(items[position].item.url)
            }
        }

    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerUrlItem(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addItems(itemsFromDetail: List<ItemsList>) {
        this.items.apply {
            clear()
            addAll(itemsFromDetail)
            notifyDataSetChanged()
        }
    }



}