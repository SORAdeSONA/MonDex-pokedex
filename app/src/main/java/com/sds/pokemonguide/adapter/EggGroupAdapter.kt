package com.sds.pokemonguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sds.pokemonguide.R
import com.sds.pokemonguide.model.TypesResult

class EggGroupAdapter : RecyclerView.Adapter<EggGroupAdapter.DataViewHolder>() {

    private var eggGroups: ArrayList<TypesResult> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.ability_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = eggGroups.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val everyEggGroupName = "${eggGroups[position].typeName} \uD83D\uDD0D"

        holder.itemView.setOnClickListener {
            println("eggGroup click")
            onItemClickListenerUrl?.let {

            }
        }

        holder.itemView.findViewById<TextView>(R.id.id_move_name).text = everyEggGroupName.replace("-", " ")
    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerUrlAbility(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addTypes(eggGroupsFromFragment: List<TypesResult>) {
        this.eggGroups.apply {
            addAll(eggGroupsFromFragment)
            notifyItemInserted(0)
        }
    }



}