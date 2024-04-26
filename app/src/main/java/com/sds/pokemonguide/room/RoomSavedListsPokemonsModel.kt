package com.sds.pokemonguide.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_lists_pokemons", primaryKeys = ["pokemonName", "listName"])
data class RoomSavedListsPokemonsModel(
    val pokemonName: String,
    val pokemonUrl:String,
    var listName: String
)