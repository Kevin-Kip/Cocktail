package com.truekenyan.cocktail.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.truekenyan.cocktail.callbacks.CocktailDao
import com.truekenyan.cocktail.models.Fav
import com.truekenyan.cocktail.utils.Commons

@Database(entities = [Fav::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coctailDao(): CocktailDao
    companion object {
        private var db: AppDatabase? = null

        fun getInstance(context: Context?): AppDatabase?{
            if (db == null){
                synchronized(AppDatabase::class){
                    db = Room.databaseBuilder(context!!, AppDatabase::class.java, Commons.DRINKS)
                            .build()
                }
            }
            return db!!
        }
    }
}