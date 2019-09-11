package com.truekenyan.cocktail.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.truekenyan.cocktail.utils.Commons

@Entity(tableName = Commons.DRINKS)
data class Fav(@PrimaryKey(autoGenerate = true) var favId: Int?,
               var drinkId: String? = "",
               var drinkName: String? = "",
               var drinkPhoto: String? = "")