package com.sds.pokemonguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sds.pokemonguide.R
import com.sds.pokemonguide.model.CallbackList
import com.sds.pokemonguide.model.ListAttributes

class ItemAttributesAdapter : RecyclerView.Adapter<ItemAttributesAdapter.DataViewHolder>() {

    private var attributes: ArrayList<ListAttributes> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.attribute_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = attributes.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.id_attribute_name).text = attributes[position].name.replace("-", " ")

        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(attributes[position].url)
            }
        }
    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerUrlAttribute(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addAttributes(attributes: List<ListAttributes>) {
        this.attributes.apply {
            clear()
            addAll(attributes)
            notifyDataSetChanged()
        }
    }
}


