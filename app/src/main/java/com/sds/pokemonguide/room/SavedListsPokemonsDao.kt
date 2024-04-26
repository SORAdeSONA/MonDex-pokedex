package com.sds.pokemonguide.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedListsPokemonsDao {

    @Query("SELECT * FROM saved_lists_pokemons")
    fun getAllPokemons(): List<RoomSavedListsPokemonsModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPokemon(item: RoomSavedListsPokemonsModel)

    @Query("DELETE FROM saved_lists_pokemons WHERE pokemonName = :pokemonNameToDelete AND listName == :fromListToDelete ")
    fun deletePokemon(pokemonNameToDelete: String, fromListToDelete:String)

    @Query("UPDATE saved_lists_pokemons SET listName = :newListName WHERE listName = :oldListName")
    fun updateListNameForPokemons(oldListName: String, newListName: String)

    @Query("DELETE FROM saved_lists_pokemons WHERE listName == :listName")
    fun deletePokemonWhenDeleteList(listName:String)

}