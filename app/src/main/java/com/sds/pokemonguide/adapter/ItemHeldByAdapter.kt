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
import com.sds.pokemonguide.model.CallbackList
import com.sds.pokemonguide.model.ListHeldByPokemon

class ItemHeldByAdapter : RecyclerView.Adapter<ItemHeldByAdapter.DataViewHolder>() {

    private var heldByList: ArrayList<ListHeldByPokemon> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.pokemon_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = heldByList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.id_pokemon_name).text = heldByList[position].pokemon.name

        val pokemonImage = heldByList[position].pokemon.url
        val id = pokemonImage.removePrefix(CONSTANTS.BASE_POKEMON_PREFIX).dropLast(1)


        Glide.with(holder.itemView.findViewById<ImageView>(R.id.id_pokemon_image).context)
            .load(CONSTANTS.getPokemonSpritesById(id))
            .into(holder.itemView.findViewById<ImageView>(R.id.id_pokemon_image))


        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(CallbackList(url = heldByList[position].pokemon.url, image = heldByList[position].pokemon.url))
            }
        }
    }

    private var onItemClickListenerUrl: ((CallbackList) -> Unit)? = null

    fun setOnItemClickListenerUrlHeldBy(listener: (CallbackList) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addHeldByPokemons(heldByFromItem: List<ListHeldByPokemon>) {
        this.heldByList.apply {
            clear()
            addAll(heldByFromItem)
            notifyDataSetChanged()
        }
    }



}