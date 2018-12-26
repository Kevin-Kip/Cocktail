package com.truekenyan.cocktail.callbacks

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface cocktailDao {

    @Query("SELECT * FROM 'drink-db'")
    fun getFavs(): List<String>

    @Insert
    fun addToFavs(drinkId: String?)

    @Delete
    fun removeFromFavs(drinkId: String?)

    @Delete
    fun clearFavs()
}