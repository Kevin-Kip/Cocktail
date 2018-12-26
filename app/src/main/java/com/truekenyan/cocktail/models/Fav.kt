package com.truekenyan.cocktail.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.truekenyan.cocktail.utils.Commons

@Entity(tableName = Commons.DRINKS)
data class Fav(@PrimaryKey(autoGenerate = true) var favId: Int?,
               var drinkId: String?,
               var drinkName: String?,
               var drinkPhoto: String?)