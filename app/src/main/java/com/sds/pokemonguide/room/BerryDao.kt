package com.sds.pokemonguide.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BerryDao {

    @Query("SELECT * FROM berries")
    fun getAllBerries(): List<RoomBerryModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBerry(item: RoomBerryModel)

    @Query("DELETE FROM berries WHERE berryName = :berryNameToDelete")
    fun deleteBerry(berryNameToDelete: String)

}