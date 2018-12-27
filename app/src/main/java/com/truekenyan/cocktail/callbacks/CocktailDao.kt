package com.truekenyan.cocktail.callbacks

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.truekenyan.cocktail.models.Fav
import com.truekenyan.cocktail.utils.Commons

@Dao
interface CocktailDao {

    @Query("SELECT * FROM ${Commons.DRINKS}")
    fun getFavs(): MutableList<Fav?>

    @Insert
    fun addToFavs(drink: Fav?)

    @Delete
    fun removeFromFavs(drink: Fav?)

    @Query("SELECT 1 FROM ${Commons.DRINKS} WHERE")
    fun getOne(drinkName: String?): Fav?
}