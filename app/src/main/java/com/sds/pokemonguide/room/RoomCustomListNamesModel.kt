package com.sds.pokemonguide.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_list_names")
data class RoomCustomListNamesModel(
    @PrimaryKey(autoGenerate = true)
    val dbId: Int = 0,
    val name: String,
    val description: String
)