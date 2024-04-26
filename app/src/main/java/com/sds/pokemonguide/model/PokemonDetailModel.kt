package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class PokemonDetailModel(
    @SerializedName("name")
    val pokemonName: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("base_experience")
    val baseExperience : Int,
    @SerializedName("id")
    val id:Int,
    @SerializedName("held_items")
    val items: List<ItemsList>,
    @SerializedName("stats")
    val stats: List<StatsList>,
    @SerializedName("moves")
    val moves : List<MovesList>,
    @SerializedName("abilities")
    val abilities: List<AbilitiesList>,
    @SerializedName("types")
    val types: List<Types>
)

data class ItemsList(
    @SerializedName("item")
    val item: Item
)

data class Item(
    @SerializedName("name")
    val name : String,
    @SerializedName("url")
    val url: String
)
data class StatsList(
    @SerializedName("base_stat")
    var baseStat: Int,
    @SerializedName("stat")
    val stat: StatNameList
)

data class AbilitiesList(
    @SerializedName("ability")
    val ability: Ability
)

data class Ability(
    @SerializedName("name")
    val abilityName: String,
    @SerializedName("url")
    val abilityUrl: String
)

data class MovesList (
    @SerializedName("move")
    val move : Move
)

data class Move (
    @SerializedName("name")
    val moveName : String,
    @SerializedName("url")
    val url : String
)

data class StatNameList(
    @SerializedName("name")
    val statName: String,
    @SerializedName("url")
    val url: String
)

data class Types(
    @SerializedName("type")
    val type: TypeName
)

data class TypeName(
    @SerializedName("name")
    val typeName: String,
    @SerializedName("url")
    val url: String
)


