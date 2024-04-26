package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class HabitatDetailModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("pokemon_species")
    val pokemonSpecies: List<PokemonSpeciesListHabitat>
)

data class PokemonSpeciesListHabitat(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)
