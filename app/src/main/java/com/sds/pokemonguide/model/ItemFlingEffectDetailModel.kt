package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class ItemFlingEffectDetailModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("effect_entries")
    val effectEntries: List<EffectEntriesList>,
    @SerializedName("items")
    val items: List<ItemsFlingEffectList>
)

data class ItemsFlingEffectList(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class EffectEntriesList(
    @SerializedName("effect")
    val effect: String
)
