package com.sds.pokemonguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sds.pokemonguide.R
import com.sds.pokemonguide.model.CategoriesList

class ItemPocketAdapter : RecyclerView.Adapter<ItemPocketAdapter.DataViewHolder>() {

    private var categories: ArrayList<CategoriesList> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.ability_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val everyItemName = categories[position].name

        holder.itemView.findViewById<TextView>(R.id.id_move_name).text = everyItemName.replace("-", " ")

        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(categories[position].url)
            }
        }
    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerUrlPocket(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addCategories(categoriesFromItemCategoryFragment: List<CategoriesList>) {
        this.categories.apply {
            clear()
            addAll(categoriesFromItemCategoryFragment)
            notifyDataSetChanged()
        }
    }
}
