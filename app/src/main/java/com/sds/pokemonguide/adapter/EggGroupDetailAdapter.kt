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
import com.sds.pokemonguide.model.PokemonSpeciesModelEggGroup

class EggGroupDetailAdapter : RecyclerView.Adapter<EggGroupDetailAdapter.DataViewHolder>() {

    private var pokemons: ArrayList<PokemonSpeciesModelEggGroup> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.main_pokemon_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = pokemons.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val everyPokemon = pokemons[position].name

        val id = pokemons[position].url.removePrefix("https://pokeapi.co/api/v2/pokemon-species/").dropLast(1)
        Glide.with(holder.itemView.findViewById<ImageView>(R.id.id_pokemon_image).context)
            .load(CONSTANTS.getPokemonSpritesById(id))
            .into(holder.itemView.findViewById<ImageView>(R.id.id_pokemon_image))

        holder.itemView.findViewById<TextView>(R.id.id_pokemon_name).text = everyPokemon.replace("-", " ")

        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(pokemons[position].url.replace("pokemon-species", "pokemon"))
            }
        }
    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerUrlEggGroup(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addPokemons(pokemonsFromFragment: List<PokemonSpeciesModelEggGroup>) {
        this.pokemons.apply {
            clear()
            addAll(pokemonsFromFragment)
            notifyDataSetChanged()
        }
    }



}