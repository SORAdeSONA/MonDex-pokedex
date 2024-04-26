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
import com.sds.pokemonguide.model.BerriesList
import com.sds.pokemonguide.model.ListFlavors

class BerryFlavorDetailAdapter : RecyclerView.Adapter<BerryFlavorDetailAdapter.DataViewHolder>() {

    private var flavors: ArrayList<ListFlavors> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.berry_flavor_detail_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = flavors.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val everyFlavor = flavors[position].flavor.name

        holder.itemView.findViewById<TextView>(R.id.id_move_name).text = everyFlavor
        holder.itemView.findViewById<TextView>(R.id.id_potency_value).text = "potency: " + flavors[position].potency

        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(flavors[position].flavor.url)
            }
        }
    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerUrlBerryFlavor(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addFlavors(berriesFlavorsFromDetail: List<ListFlavors>) {
        this.flavors.apply {
            clear()
            addAll(berriesFlavorsFromDetail)
            notifyDataSetChanged()
        }
    }



}