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
import com.sds.pokemonguide.room.RoomBerryModel
import com.sds.pokemonguide.room.RoomItemModel

class SavedBerriesAdapter : RecyclerView.Adapter<SavedBerriesAdapter.DataViewHolder>() {

    private var savedBerries: ArrayList<RoomBerryModel> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.main_item_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = savedBerries.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val berryName = savedBerries[position].berryName

        Glide.with(holder.itemView.findViewById<ImageView>(R.id.id_item_image).context)
            .load(CONSTANTS.getBerrySpritesByName(berryName))
            .into(holder.itemView.findViewById<ImageView>(R.id.id_item_image))

        holder.itemView.findViewById<TextView>(R.id.id_item_name).text = berryName.replace("-", " ")

        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(savedBerries[position].berryUrl)
            }
        }
    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerUrl(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addBerries(berriesFromDb: List<RoomBerryModel>) {
        this.savedBerries.apply {
            clear()
            addAll(berriesFromDb)
            notifyDataSetChanged()
        }
    }



}