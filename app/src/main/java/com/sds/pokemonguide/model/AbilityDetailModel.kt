package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class AbilityDetailModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("is_main_series")
    val isMainSeries: Boolean,
    @SerializedName("generation")
    val generation: GenerationClassAbility,
    @SerializedName("pokemon")
    val pokemonsWithAbility: List<ListPokemonsWithAbility>,
    @SerializedName("effect_entries")
    val effectEntriesAbility: List<ListEffectEntriesAbility>,
    @SerializedName("flavor_text_entries")
    val flavorTextEntriesAbility: List<ListFlavorTextEntriesAbility>
)

data class ListFlavorTextEntriesAbility(
    @SerializedName("flavor_text")
    val flavorText: String,
    @SerializedName("language")
    val language: LanguageClass,
    @SerializedName("version_group")
    val versionGroup: VersionGroupClass
)



data class VersionGroupClass(
    @SerializedName("name")
    val groupName: String,
    @SerializedName("url")
    val groupUrl: String
)

data class ListEffectEntriesAbility(
    @SerializedName("effect")
    val effect: String,
    @SerializedName("short_effect")
    val shortEffect: String,
    @SerializedName("language")
    val language: LanguageClass
)

data class LanguageClass(
    @SerializedName("name")
    val languageName: String
)

data class ListPokemonsWithAbility(
    @SerializedName("pokemon")
    val pokemon: PokemonsValues
)

data class PokemonsValues(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class GenerationClassAbility(
    @SerializedName("name")
    val generationName: String,
    @SerializedName("url")
    val generationUrl: String
)
