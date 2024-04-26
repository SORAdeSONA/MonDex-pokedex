package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class EggGroupDetailModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("pokemon_species")
    val pokemonSpecies: List<PokemonSpeciesModelEggGroup>
)

data class PokemonSpeciesModelEggGroup(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url:String
)
