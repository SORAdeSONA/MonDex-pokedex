package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class ItemPocketDetailModel(
    @SerializedName("name")
    val name:String,
    @SerializedName("categories")
    val categories: List<CategoriesList>
)

data class CategoriesList(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)
