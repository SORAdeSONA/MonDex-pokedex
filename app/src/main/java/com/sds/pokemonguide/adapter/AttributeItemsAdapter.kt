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
import com.sds.pokemonguide.model.ListItems

class AttributeItemsAdapter : RecyclerView.Adapter<AttributeItemsAdapter.DataViewHolder>() {

    private var items: ArrayList<ListItems> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.main_item_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        holder.itemView.findViewById<TextView>(R.id.id_item_name).text = items[position].name.replace("-", " ")

        Glide.with(holder.itemView.findViewById<ImageView>(R.id.id_item_image).context)
            .load(CONSTANTS.getItemSpritesByName(items[position].name))
            .into(holder.itemView.findViewById<ImageView>(R.id.id_item_image))

        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(items[position].url)
            }

            /*items.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.name }))
            notifyDataSetChanged() */
        }

    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerAttributeItem(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addItemsWithAttribute(itemsWithAttribute: List<ListItems>) {
        this.items.apply {
            clear()
            addAll(itemsWithAttribute)
            notifyDataSetChanged()
        }
    }



}