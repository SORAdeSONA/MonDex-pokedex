package com.sds.pokemonguide.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemons")
    fun getAllPokemons(): List<RoomPokemonModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPokemon(pokemon: RoomPokemonModel)

    @Query("DELETE FROM pokemons WHERE pokemonName = :pokemonNameToDelete")
    fun deletePokemon(pokemonNameToDelete: String)

}