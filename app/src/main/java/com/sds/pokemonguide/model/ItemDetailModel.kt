package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class ItemDetailModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("cost")
    val cost: Int,
    @SerializedName("category")
    val category: Category,
    @SerializedName("fling_power")
    val fling_power: Int,
    @SerializedName("attributes")
    val attributes: List<ListAttributes>,
    @SerializedName("sprites")
    val sprite: Sprite,
    @SerializedName("effect_entries")
    val effectEntries: List<ListEffectEntriesItem>,
    @SerializedName("held_by_pokemon")
    val heldByPokemon: List<ListHeldByPokemon>,
    @SerializedName("flavor_text_entries")
    val flavor_text_entries: List<ListFlavorTextEntriesItem>
)

data class ListFlavorTextEntriesItem(
    @SerializedName("text")
    val text : String,
    @SerializedName("language")
    val language: LanguageClass
)

data class ListHeldByPokemon(
    @SerializedName("pokemon")
    val pokemon: PokemonHeldValues
)

data class PokemonHeldValues(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class ListEffectEntriesItem(
    @SerializedName("effect")
    val effect: String,
    @SerializedName("short_effect")
    val shortEffect: String,
    @SerializedName("language")
    val language: LanguageClass
)

data class Sprite(
    @SerializedName("default")
    val defaultSprite: String
)

data class ListAttributes(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class Category(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)
