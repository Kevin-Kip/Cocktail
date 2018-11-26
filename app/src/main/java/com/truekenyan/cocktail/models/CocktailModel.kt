package com.truekenyan.cocktail.models

class CocktailModel {
    var drinkId: Int = 0
    var imagePath: String? = null
    var drinkName: String? = null
    var method: String? = null
    var isAlcoholic: String? = null
    var drinkCategory: String? = null
    var glass: String? = null
    var ingredients: Map<String, String>? = null

    constructor() {}

    constructor(drinkId: Int,
                imagePath: String,
                drinkName: String,
                method: String,
                isAlcoholic: String,
                drinkCategory: String,
                glass: String,
                ingredients: Map<String, String>) {
        this.drinkId = drinkId
        this.imagePath = imagePath
        this.drinkName = drinkName
        this.method = method
        this.isAlcoholic = isAlcoholic
        this.drinkCategory = drinkCategory
        this.glass = glass
        this.ingredients = ingredients
    }
}