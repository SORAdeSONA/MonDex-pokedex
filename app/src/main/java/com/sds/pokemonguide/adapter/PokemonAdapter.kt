package com.sds.pokemonguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sds.pokemonguide.CONSTANTS
import com.sds.pokemonguide.R
import com.sds.pokemonguide.model.CallbackList
import com.sds.pokemonguide.model.Result


class PokemonAdapter() : PagingDataAdapter<Result, PokemonAdapter.ViewHolder>(DataDifferntiator) {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.main_pokemon_rv_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val pokemonName = getItem(position)?.name

        val pokemonImage = getItem(position)!!.url
        val id = pokemonImage.removePrefix(CONSTANTS.BASE_POKEMON_PREFIX).dropLast(1)

        holder.itemView.findViewById<TextView>(R.id.id_pokemon_name).text = "#$id $pokemonName"

        Glide.with(holder.itemView.findViewById<ImageView>(R.id.id_pokemon_image).context)
            .load(CONSTANTS.getPokemonSpritesById(id))
            .into(holder.itemView.findViewById<ImageView>(R.id.id_pokemon_image))

        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(CallbackList(url = getItem(position)!!.url, image = pokemonImage))
            }
        }
    }


    private var onItemClickListenerUrl: ((CallbackList) -> Unit)? = null


    fun setOnItemClickListenerUrl(listener: (CallbackList) -> Unit) {
        onItemClickListenerUrl = listener
    }


    object DataDifferntiator : DiffUtil.ItemCallback<Result>() {

        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

}

