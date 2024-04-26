package com.sds.pokemonguide.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class RoomItemModel(
    @PrimaryKey(autoGenerate = true)
    val dbId: Int = 0,
    val itemName: String,
    val itemUrl: String
)