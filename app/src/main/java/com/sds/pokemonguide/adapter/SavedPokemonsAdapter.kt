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
import com.sds.pokemonguide.room.RoomPokemonModel

class SavedPokemonsAdapter : RecyclerView.Adapter<SavedPokemonsAdapter.DataViewHolder>() {

    private var savedPokemons: ArrayList<RoomPokemonModel> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.main_pokemon_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = savedPokemons.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val pokemonName = savedPokemons[position].pokemonName

        val pokemonImage = savedPokemons[position].pokemonUrl
        val id = pokemonImage.removePrefix(CONSTANTS.BASE_POKEMON_PREFIX).dropLast(1)

        Glide.with(holder.itemView.findViewById<ImageView>(R.id.id_pokemon_image).context)
            .load(CONSTANTS.getPokemonSpritesById(id))
            .into(holder.itemView.findViewById<ImageView>(R.id.id_pokemon_image))

        holder.itemView.findViewById<TextView>(R.id.id_pokemon_name).text = "#$id $pokemonName"

        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(savedPokemons[position].pokemonUrl)
            }
        }
    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerUrl(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addPokemons(pokemonsFromDb: List<RoomPokemonModel>) {
        this.savedPokemons.apply {
            clear()
            addAll(pokemonsFromDb)
            notifyDataSetChanged()
        }
    }



}