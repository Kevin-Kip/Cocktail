package com.truekenyan.cocktail.models

class CocktailModel(var idDrink: String?, var strDrinkThumb: String?, var strDrink: String?, var strInstructions: String?, var strAlcoholic: String?, var strCategory: String?, var strGlass: String?) {
    var ingredients: Map<String, String>? = null
}
