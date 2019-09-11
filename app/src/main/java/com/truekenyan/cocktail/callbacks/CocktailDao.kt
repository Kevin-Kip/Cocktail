package com.truekenyan.cocktail.callbacks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.truekenyan.cocktail.models.Fav
import com.truekenyan.cocktail.utils.Commons

@Dao
interface CocktailDao {

    @Query("SELECT * FROM ${Commons.DRINKS} ORDER BY ${Commons.FAV_ID} DESC")
    fun getFavs(): MutableList<Fav>

    @Insert
    fun addToFavs(drink: Fav?)

    @Delete
    fun removeFromFavs(drink: Fav?)

    @Query("SELECT * FROM ${Commons.DRINKS} WHERE ${Commons.DRINK_NAME} = :drinkName")
    fun getOne(drinkName: String?): MutableList<Fav?>

    @Query("DELETE FROM ${Commons.DRINKS}")
    fun clearFavorites()
}