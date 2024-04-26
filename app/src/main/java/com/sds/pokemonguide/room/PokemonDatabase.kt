package com.sds.pokemonguide.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomPokemonModel::class,
    RoomItemModel::class,
    RoomBerryModel::class,
    RoomMoveModel::class,
    RoomCustomListNamesModel::class,
    RoomSavedListsPokemonsModel::class], version = 1)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
    abstract fun itemDao(): ItemDao
    abstract fun berryDao(): BerryDao
    abstract fun moveDao(): MoveDao
    abstract fun customListNamesDao() : CustomListNamesDao
    abstract fun savedListsPokemonsDao() : SavedListsPokemonsDao

}
