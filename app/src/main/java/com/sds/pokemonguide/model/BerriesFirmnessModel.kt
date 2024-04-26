package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class BerriesFirmnessModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("berries")
    val berriesList: List<BerriesDataList>
)

data class BerriesDataList(
    @SerializedName("name")
    val berrieName: String,
    @SerializedName("url")
    val berrieUrl: String
)


