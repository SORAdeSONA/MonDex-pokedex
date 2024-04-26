package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class CommonListModel(
    val count: Long,

    @SerializedName("next")
    val next: String,

    @SerializedName("results")
    val results: List<Result>
)

data class Result(

    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String
)
