package com.sds.pokemonguide.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MoveDao {

    @Query("SELECT * FROM moves")
    fun getAllMoves(): List<RoomMoveModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMove(item: RoomMoveModel)

    @Query("DELETE FROM moves WHERE moveName = :moveNameToDelete")
    fun deleteMove(moveNameToDelete: String)

}