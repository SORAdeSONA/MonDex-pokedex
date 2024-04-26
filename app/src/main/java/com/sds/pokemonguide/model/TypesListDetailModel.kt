package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class TypesListDetailModel(
    @SerializedName("results")
    val results: List<TypesResult>
)

data class TypesResult(
    @SerializedName("name")
    val typeName: String,
    @SerializedName("url")
    val typeUrl: String
)
