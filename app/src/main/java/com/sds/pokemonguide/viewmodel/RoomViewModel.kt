package com.sds.pokemonguide.viewmodel

import androidx.lifecycle.ViewModel
import com.sds.pokemonguide.room.RoomBerryModel
import com.sds.pokemonguide.room.RoomCustomListNamesModel
import com.sds.pokemonguide.room.RoomItemModel
import com.sds.pokemonguide.room.RoomMoveModel
import com.sds.pokemonguide.room.RoomPokemonModel
import com.sds.pokemonguide.room.RoomRepository
import com.sds.pokemonguide.room.RoomSavedListsPokemonsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {
    fun getAllPokemonsFromDb(): List<RoomPokemonModel> {
        return roomRepository.getAllPokemons()
    }

    fun addPokemonToDb(pokemonName: String, pokemonUrl: String) {
        roomRepository.addPokemon(RoomPokemonModel(pokemonName, pokemonUrl))
    }

    fun deletePokemonFromDb(pokemonName: String) {
        roomRepository.deletePokemon(pokemonName)
    }

    fun getAllItemsFromDb(): List<RoomItemModel> {
        return roomRepository.getAllItems()
    }

    fun addItemToDb(dbId: Int, itemName: String, itemUrl: String) {
        roomRepository.addItem(RoomItemModel(dbId, itemName, itemUrl))
    }

    fun deleteItemFromDb(itemName: String) {
        roomRepository.deleteItem(itemName)
    }

    fun getAllBerriesFromDb(): List<RoomBerryModel> {
        return roomRepository.getAllBerries()
    }

    fun addBerryToDb(dbId: Int, berryName: String, berryUrl: String) {
        roomRepository.addBerry(RoomBerryModel(dbId, berryName, berryUrl))
    }

    fun deleteBerryFromDb(berryName: String) {
        roomRepository.deleteBerry(berryName)
    }

    fun getAllMovesFromDb(): List<RoomMoveModel> {
        return roomRepository.getAllMoves()
    }

    fun addMoveToDb(dbId: Int, moveName: String, moveUrl: String) {
        roomRepository.addMove(RoomMoveModel(dbId, moveName, moveUrl))
    }

    fun deleteMoveFromDb(moveName: String) {
        roomRepository.deleteMove(moveName)
    }

    suspend fun getAllLists(): List<RoomCustomListNamesModel> {
        return roomRepository.getAllCategories()

    }

    fun addListToDb(dbId: Int, listName: String, listDescription: String) {
        roomRepository.addCategory(RoomCustomListNamesModel(dbId, listName, listDescription))
    }

    fun deleteListFromDb(listName: String) {
        roomRepository.deleteCategory(listName)
    }

    fun updateListNameAndDescription(
        oldCategoryName: String,
        newCategoryName: String,
        oldCategoryDescription: String,
        newCategoryDescription: String
    ) {
        roomRepository.updateCategoryNameAndDescription(
            oldCategoryName,
            newCategoryName,
            oldCategoryDescription,
            newCategoryDescription
        )
    }

    fun getAllSavedListsPokemons(): List<RoomSavedListsPokemonsModel> {
        return roomRepository.getAllSavedListsPokemons()
    }

    fun addPokemonToSavedLists(pokemonName: String, pokemonUrl: String, listName: String) {
        roomRepository.addPokemonToSavedLists(
            RoomSavedListsPokemonsModel(
                pokemonName,
                pokemonUrl,
                listName
            )
        )
    }

    fun deletePokemonFromSavedLists(pokemonName: String, listName: String) {
        roomRepository.deletePokemonFromSavedLists(pokemonName, listName)
    }

    fun deletePokemonWhenDeleteList(listName: String) {
        roomRepository.deletePokemonWhenDeleteList(listName)
    }

    fun updateListNameForPokemons(oldListName: String, newListName: String) {
        roomRepository.updateListNameForPokemons(oldListName, newListName)
    }

}