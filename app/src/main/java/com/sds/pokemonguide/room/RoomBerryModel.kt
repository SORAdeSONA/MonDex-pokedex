package com.sds.pokemonguide.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "berries")
data class RoomBerryModel(
    @PrimaryKey(autoGenerate = true)
    val dbId: Int = 0,
    val berryName: String,
    val berryUrl: String
)