package com.sds.pokemonguide.room

import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val pokemonDao: PokemonDao,
    private val itemDao: ItemDao,
    private val berryDao: BerryDao,
    private val moveDao: MoveDao,
    private val customListNamesDao: CustomListNamesDao,
    private val savedListsPokemonsDao: SavedListsPokemonsDao,
) {

    fun getAllPokemons(): List<RoomPokemonModel> {
        return pokemonDao.getAllPokemons()
    }

    fun addPokemon(pokemon: RoomPokemonModel) {
        pokemonDao.addPokemon(pokemon)
    }

    fun deletePokemon(pokemonName: String) {
        pokemonDao.deletePokemon(pokemonName)
    }

    fun getAllItems(): List<RoomItemModel> {
        return itemDao.getAllItems()
    }

    fun addItem(item: RoomItemModel) {
        itemDao.addItem(item)
    }

    fun deleteItem(itemName: String) {
        itemDao.deleteItem(itemName)
    }

    fun getAllBerries(): List<RoomBerryModel> {
        return berryDao.getAllBerries()
    }

    fun addBerry(item: RoomBerryModel) {
        berryDao.addBerry(item)
    }

    fun deleteBerry(berryName: String) {
        berryDao.deleteBerry(berryName)
    }

    fun getAllMoves(): List<RoomMoveModel> {
        return moveDao.getAllMoves()
    }

    fun addMove(move: RoomMoveModel) {
        moveDao.addMove(move)
    }

    fun deleteMove(berryName: String) {
        moveDao.deleteMove(berryName)
    }

    fun getAllCategories(): List<RoomCustomListNamesModel> {
        return customListNamesDao.getAllCategories()
    }

    fun addCategory(category: RoomCustomListNamesModel) {
        customListNamesDao.addCategory(category)
    }

    fun deleteCategory(categoryName: String) {
        customListNamesDao.deleteCategory(categoryName)
    }

    fun updateCategoryNameAndDescription(oldCategoryName: String,
                                         newCategoryName: String,
                                         oldCategoryDescription: String,
                                         newCategoryDescription: String) {
        customListNamesDao.updateCategoryNameAndDescription(oldCategoryName, newCategoryName, oldCategoryDescription, newCategoryDescription)
    }

    fun getAllSavedListsPokemons(): List<RoomSavedListsPokemonsModel> {
        return savedListsPokemonsDao.getAllPokemons()
    }

    fun addPokemonToSavedLists(pokemon: RoomSavedListsPokemonsModel) {
        savedListsPokemonsDao.addPokemon(pokemon)
    }

    fun deletePokemonFromSavedLists(pokemonName: String, listName: String) {
        savedListsPokemonsDao.deletePokemon(pokemonName, listName)
    }

    fun deletePokemonWhenDeleteList(listName: String) {
        savedListsPokemonsDao.deletePokemonWhenDeleteList(listName)
    }

    fun updateListNameForPokemons(oldListName: String, newListName: String) {
        savedListsPokemonsDao.updateListNameForPokemons(oldListName, newListName)
    }



}