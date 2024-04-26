package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class BerryDetailModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("item")
    val item: ItemClass,
    @SerializedName("natural_gift_power")
    val naturalGiftPower: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("smoothness")
    val smoothness: Int,
    @SerializedName("soil_dryness")
    val soilDryness: Int,
    @SerializedName("growth_time")
    val growthTime: Int,
    @SerializedName("max_harvest")
    val maxHarvest: Int,
    @SerializedName("natural_gift_type")
    val naturalGiftType: NaturalGiftTypeClass,
    @SerializedName("firmness")
    val firmness: FirmnessClass,
    @SerializedName("flavors")
    val flavors: List<ListFlavors>
)

data class ItemClass(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class ListFlavors(
    @SerializedName("flavor")
    val flavor: FlavorsClass,
    @SerializedName("potency")
    val potency: Int
)

data class FlavorsClass(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class FirmnessClass(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class NaturalGiftTypeClass(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)
