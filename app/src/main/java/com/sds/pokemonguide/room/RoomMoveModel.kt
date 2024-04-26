package com.sds.pokemonguide.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moves")
data class RoomMoveModel(
    @PrimaryKey(autoGenerate = true)
    val dbId: Int = 0,
    val moveName: String,
    val moveUrl: String
)