package com.truekenyan.cocktail.holders

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.models.Ingredient

class IngredientsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ingredientName = itemView.findViewById<TextView>(R.id.ingredient)
    @SuppressLint("SetTextI18n")
    fun bind(ingredient: Ingredient){
        ingredientName.text = ingredient.measure + " "+ ingredient.name
    }
}