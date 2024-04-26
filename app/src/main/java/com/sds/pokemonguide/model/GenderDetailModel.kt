package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class GenderDetailModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("pokemon_species_details")
    val pokemonSpeciesDetailList: List<PokemonSpeciesList>
)

data class PokemonSpeciesList(
    @SerializedName("pokemon_species")
    val pokemonSpeciesModel: PokemonSpeciesModelGender
)

data class PokemonSpeciesModelGender(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)
