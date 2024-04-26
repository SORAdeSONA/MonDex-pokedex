package com.sds.pokemonguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sds.pokemonguide.R
import com.sds.pokemonguide.model.TypesResult

class CommonCategoryAdapter : RecyclerView.Adapter<CommonCategoryAdapter.DataViewHolder>() {

    private var types: ArrayList<TypesResult> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.common_item_rv_category_layout, parent,
                false
            )
        )

    override fun getItemCount(): Int = types.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val everyTypeName = "${types[position].typeName}"

        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(types[position].typeUrl)
            }
        }

        holder.itemView.findViewById<TextView>(R.id.id_common_item_rv_name).text = everyTypeName.replace("-", " ")
    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerUrlCommonCategory(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addTypes(types: List<TypesResult>) {
        this.types.apply {
            addAll(types)
            notifyItemInserted(0)
        }
    }



}