package com.truekenyan.cocktail.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.holders.FavsViewHolder
import com.truekenyan.cocktail.models.Fav

class FavoritesAdapter(val context: Context, var favorites: List<Fav?>): RecyclerView.Adapter<FavsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): FavsViewHolder {
        val rootView = LayoutInflater.from(context).inflate(R.layout.item_fav, parent, false)
        return FavsViewHolder(rootView)
    }

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: FavsViewHolder, position: Int) = holder.bind(favorites[position]!!)

    fun setItems(favsList: MutableList<Fav?>) {
        favorites = favsList
        notifyDataSetChanged()
    }
}