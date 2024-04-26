package com.sds.pokemonguide.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CustomListNamesDao {

    @Query("SELECT * FROM custom_list_names")
    fun getAllCategories(): List<RoomCustomListNamesModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(item: RoomCustomListNamesModel)

    @Query("DELETE FROM custom_list_names WHERE name = :categoryNameToDelete")
    fun deleteCategory(categoryNameToDelete: String)

    @Query("UPDATE custom_list_names SET name = :newCategoryName, description = :newCategoryDescription WHERE name = :oldCategoryName AND description = :oldCategoryDescription")
    fun updateCategoryNameAndDescription
                (oldCategoryName: String,
                 newCategoryName: String,
                 oldCategoryDescription: String,
                 newCategoryDescription: String)

}