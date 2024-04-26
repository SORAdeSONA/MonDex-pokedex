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
import com.sds.pokemonguide.model.ListLearnedByPokemon
import com.sds.pokemonguide.model.ListPokemonsWithAbility

class AbilityUsedByPokemonsAdapter : RecyclerView.Adapter<AbilityUsedByPokemonsAdapter.DataViewHolder>() {

    private var pokemons: ArrayList<ListPokemonsWithAbility> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.pokemon_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = pokemons.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        holder.itemView.findViewById<TextView>(R.id.id_pokemon_name).text = pokemons[position].pokemon.name.replace("-", " ")

        println(pokemons[position].pokemon.name)
        val pokemonImage = pokemons[position].pokemon.url
        val id = pokemonImage.removePrefix(CONSTANTS.BASE_POKEMON_PREFIX).dropLast(1)


        Glide.with(holder.itemView.findViewById<ImageView>(R.id.id_pokemon_image).context)
            .load(CONSTANTS.getPokemonSpritesById(id))
            .into(holder.itemView.findViewById<ImageView>(R.id.id_pokemon_image))

        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(pokemons[position].pokemon.url)
            }
        }

        //holder.itemView.findViewById<TextView>(R.id.id_move_name).text = everyMove
    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerUrlAbility(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addAlsoUsedByPokemons(alsoUsedByPokemons: List<ListPokemonsWithAbility>) {
        this.pokemons.apply {
            clear()
            addAll(alsoUsedByPokemons)
            notifyDataSetChanged()
        }
    }



}