package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class MoveAilmentDetailModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("moves")
    val moves: List<MovesDetailList>
)

data class MovesDetailList(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)
