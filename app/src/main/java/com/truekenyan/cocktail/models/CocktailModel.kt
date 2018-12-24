package com.truekenyan.cocktail.models

import java.io.Serializable

data class CocktailModel(var idDrink: String?,
                    var strDrinkThumb: String?,
                    var strDrink: String?,
                    var strInstructions: String?,
                    var strAlcoholic: String?,
                    var strCategory: String?,
                    var strGlass: String?
): Serializable
