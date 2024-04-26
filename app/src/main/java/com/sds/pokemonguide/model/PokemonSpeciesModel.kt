package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class PokemonSpeciesModel(
    @SerializedName("base_happiness")
    val baseHappiness: Int,
    @SerializedName("capture_rate")
    val captureRate: Int,
    @SerializedName("color")
    val color: ColorName,
    @SerializedName("evolution_chain")
    val evolutionChain: EvolutionChainUrl,
    @SerializedName("generation")
    val generation: GenerationName,
    @SerializedName("habitat")
    val habitat: HabitatName,
    @SerializedName("hatch_counter")
    val hatchCounter: Int,
    @SerializedName("shape")
    val shape: ShapeName,
    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntriesList>,
    )

data class EvolutionChainUrl(
    @SerializedName("url")
    val url: String
)

data class ShapeName(
    @SerializedName("name")
    val shapeName: String,
    @SerializedName("url")
    val url: String
)

data class HabitatName(
    @SerializedName("name")
    val habitatName: String,
    @SerializedName("url")
    val url:String
)

data class GenerationName(
    @SerializedName("name")
    val generationName: String
)

data class ColorName(
    @SerializedName("name")
    val colorName: String
)


data class FlavorTextEntriesList(
    @SerializedName("flavor_text")
    val flavorText: String,
    @SerializedName("language")
    val language: LanguageClass
)

