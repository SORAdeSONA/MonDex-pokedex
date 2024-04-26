package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class ShapeDetailModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("pokemon_species")
    val pokemonSpecies: List<PokemonSpeciesListShape>
)

data class PokemonSpeciesListShape(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)
