package com.sds.pokemonguide.model

import com.google.gson.annotations.SerializedName

data class ItemAttributeDetailModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("descriptions")
    val descriptions: List<DescriptionsListItemAttribute>,
    @SerializedName("items")
    val items: List<ItemAttributeDetailList>
)

data class DescriptionsListItemAttribute(
    @SerializedName("description")
    val description: String,
    @SerializedName("language")
    val language: LanguageClassCategoryItemAttribute
)

data class LanguageClassCategoryItemAttribute(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class ItemAttributeDetailList(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)
