package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class ItemCategoryDetailModel(
    @SerializedName("name")
    val name:String,
    @SerializedName("pocket")
    val pocket: PocketDetail,
    @SerializedName("items")
    val items: List<ItemCategoryItemsList>
)

data class ItemCategoryItemsList(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class PocketDetail(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url:String
)
