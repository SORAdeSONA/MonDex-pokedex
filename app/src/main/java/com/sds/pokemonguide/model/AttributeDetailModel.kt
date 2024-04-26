package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class AttributeDetailModel(
    @SerializedName("descriptions")
    val descriptions: List<ListDescriptions>,
    @SerializedName("name")
    val name: String,
    @SerializedName("items")
    val items: List<ListItems>
)

data class ListItems(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class ListDescriptions(
    @SerializedName("description")
    val description: String
)
