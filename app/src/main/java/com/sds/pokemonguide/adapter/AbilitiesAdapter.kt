package com.sds.pokemonguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sds.pokemonguide.R
import com.sds.pokemonguide.model.AbilitiesList

class AbilitiesAdapter : RecyclerView.Adapter<AbilitiesAdapter.DataViewHolder>() {

    private var abilities: ArrayList<AbilitiesList> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.ability_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = abilities.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val everyAbility = abilities[position].ability.abilityName

        holder.itemView.setOnClickListener {
            println("ability click")
            onItemClickListenerUrl?.let {
                it(abilities[position].ability.abilityUrl)
            }
        }

        holder.itemView.findViewById<TextView>(R.id.id_move_name).text = everyAbility.replace("-", " ")
    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerUrlAbility(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addAbilities(movesFromDetail: List<AbilitiesList>) {
        this.abilities.apply {
            clear()
            addAll(movesFromDetail)
            notifyDataSetChanged()
        }
    }



}