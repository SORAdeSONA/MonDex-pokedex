package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class MoveDetailModel(
    @SerializedName("name")
    val moveName: String,
    @SerializedName("power")
    val power: Int,
    @SerializedName("pp")
    val pp: Int,
    @SerializedName("target")
    val targetClass: TargetClass,
    @SerializedName("id")
    val id:Int,
    @SerializedName("accuracy")
    val accuracy: Int,
    @SerializedName("generation")
    val generation: GenerationClass,
    @SerializedName("meta")
    val metaClass: MetaClass,
    @SerializedName("damage_class")
    val damageClass: DamageClass,
    @SerializedName("type")
    val typeClass: TypeClass,
    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<ListFlavorTextEntries>,
    @SerializedName("effect_entries")
    val effectEntries: List<ListEffectEntries>,
    @SerializedName("learned_by_pokemon")
    val learnedByPokemon: List<ListLearnedByPokemon>
)

data class GenerationClass (
    @SerializedName("name")
    val generationName:String
)

data class TypeClass(
    @SerializedName("name")
    val typeName: String,
    @SerializedName("url")
    val typeUrl: String
)

data class MetaClass(
    @SerializedName("crit_rate")
    val critRate: Int,
    @SerializedName("drain")
    val drain: Int,
    @SerializedName("flinch_chance")
    val flinchChance: Int,
    @SerializedName("healing")
    val healing: Int,
    @SerializedName("max_hits")
    val maxHits: Int,
    @SerializedName("min_turns")
    val minTurns: Int,
    @SerializedName("stat_chance")
    val statChance: Int,
    @SerializedName("category")
    val categoryClass: CategoryClass,
    @SerializedName("ailment")
    val ailmentClass: AilmentClass,
    @SerializedName("ailment_chance")
    val ailmentChance: Int
)

data class AilmentClass(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class ListLearnedByPokemon(
    @SerializedName("name")
    val pokemonName: String,
    @SerializedName("url")
    val pokemonUrl: String
)

data class CategoryClass(
    @SerializedName("name")
    val categoryName: String,
    @SerializedName("url")
    val url:String
)

data class TargetClass(
    @SerializedName("name")
    val targetName: String,
    @SerializedName("url")
    val url: String
)

data class ListEffectEntries(
    @SerializedName("effect")
    val effect: String,
    @SerializedName("short_effect")
    val shortEffect: String,
    @SerializedName("language")
    val language: LanguageClass
)

data class ListFlavorTextEntries(
    @SerializedName("flavor_text")
    val flavorText: String,
    @SerializedName("language")
    val language: LanguageClass
)

data class DamageClass(
    @SerializedName("name")
    val name: String
)
