package com.truekenyan.cocktail.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.holders.IngredientsViewHolder
import com.truekenyan.cocktail.models.Ingredient

class IngredientsAdapter(private val context: Context, private var ingredients: List<Ingredient>)
    : RecyclerView.Adapter<IngredientsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): IngredientsViewHolder {
        val rootView = LayoutInflater.from(context).inflate(R.layout.item_ingredients, parent, false)
        return IngredientsViewHolder(rootView)
    }

    override fun getItemCount(): Int = ingredients.size

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    fun setIngredients(list: List<Ingredient>){
        ingredients = list
        notifyDataSetChanged()
    }
}