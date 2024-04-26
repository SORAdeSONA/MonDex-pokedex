package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class PokemonByTypeModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("pokemon")
    val pokemon: List<PokemonListByType>,
    @SerializedName("damage_relations")
    val damageRelations: DamageRelationsClass,
    @SerializedName("generation")
    val generation: GenerationClass,
    @SerializedName("move_damage_class")
    val moveDamageClass: MoveDamageClass,
)

data class PokemonListByType (
    @SerializedName("pokemon")
    val pokemon: PokemonModelByType
)

class PokemonModelByType(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class MoveDamageClass(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class DamageRelationsClass(
    @SerializedName("double_damage_from")
    val doubleDamageFrom: List<CommonDamageList>,
    @SerializedName("double_damage_to")
    val doubleDamageTo: List<CommonDamageList>,
    @SerializedName("half_damage_from")
    val halfDamageFrom: List<CommonDamageList>,
    @SerializedName("half_damage_to")
    val halfDamageTo : List<CommonDamageList>,
    @SerializedName("no_damage_from")
    val noDamageFrom: List<CommonDamageList>,
    @SerializedName("no_damage_to")
    val noDamageTo: List<CommonDamageList>
)

data class CommonDamageList(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)


