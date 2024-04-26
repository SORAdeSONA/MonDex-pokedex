package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class BerriesFlavorModel(
    @SerializedName("name")
    val name:String,
    @SerializedName("berries")
    val berriesList: List<BerriesList>
)

data class BerriesList(
    @SerializedName("berry")
    val berry: BerryData,
    @SerializedName("potency")
    val potency: Int
)

data class BerryData(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url:String
)
