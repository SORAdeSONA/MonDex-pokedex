package com.sds.pokemonguide.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ItemDao {

    @Query("SELECT * FROM items")
    fun getAllItems(): List<RoomItemModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(item: RoomItemModel)

    @Query("DELETE FROM items WHERE itemName = :itemNameToDelete")
    fun deleteItem(itemNameToDelete: String)

}