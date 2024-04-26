package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName



data class MoveCommonDetailModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("descriptions")
    val descriptions: List<DescriptionsList>,
    @SerializedName("moves")
    val moves: List<MovesDetailListCategory>
)

data class DescriptionsList(
    @SerializedName("description")
    val description: String,
    @SerializedName("language")
    val language: LanguageClassCategory
)

data class LanguageClassCategory(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class MovesDetailListCategory(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)
