package com.sds.pokemonguide.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pokemons")
data class RoomPokemonModel(
    @PrimaryKey
    val pokemonName: String,
    val pokemonUrl: String
)
