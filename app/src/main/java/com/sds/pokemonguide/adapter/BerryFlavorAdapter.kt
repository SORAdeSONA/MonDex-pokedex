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
import com.sds.pokemonguide.model.BerriesDataList
import com.sds.pokemonguide.model.BerriesList

class BerryFlavorAdapter : RecyclerView.Adapter<BerryFlavorAdapter.DataViewHolder>() {

    private var berries: ArrayList<BerriesList> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.berry_flavor_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = berries.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val everyBerry = berries[position].berry.name

        Glide.with(holder.itemView.findViewById<ImageView>(R.id.id_berry_image).context)
            .load(CONSTANTS.getBerrySpritesByName(everyBerry))
            .into(holder.itemView.findViewById<ImageView>(R.id.id_berry_image))

        holder.itemView.findViewById<TextView>(R.id.id_move_name).text = everyBerry.replace("-", " ")
        holder.itemView.findViewById<TextView>(R.id.id_potency_value).text = "potency: " + berries[position].potency.toString()

        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(berries[position].berry.url)
            }
        }
    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerUrlFlavor(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addBerries(berriesFromFragment: List<BerriesList>) {
        this.berries.apply {
            clear()
            addAll(berriesFromFragment)
            notifyDataSetChanged()
        }
    }



}