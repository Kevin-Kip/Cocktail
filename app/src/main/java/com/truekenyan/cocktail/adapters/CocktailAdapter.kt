package com.truekenyan.cocktail.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.holders.CocktailViewHolder
import com.truekenyan.cocktail.models.CocktailModel

class CocktailAdapter(private val context: Context, private val cocktails: ArrayList<CocktailModel>)
    : RecyclerView.Adapter<CocktailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): CocktailViewHolder {
        val rootView = LayoutInflater.from(context).inflate(R.layout.item_home_list, parent, false)
        return CocktailViewHolder(rootView)
    }

    override fun getItemCount(): Int = cocktails.size

    override fun onBindViewHolder(holder: CocktailViewHolder, position: Int) = holder.bind(cocktails[position])
}