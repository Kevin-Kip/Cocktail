package com.truekenyan.cocktail.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.adapters.FavoritesAdapter
import com.truekenyan.cocktail.callbacks.Callbacks
import com.truekenyan.cocktail.callbacks.CocktailDao
import com.truekenyan.cocktail.database.AppDatabase
import com.truekenyan.cocktail.models.Fav

class FragmentFavorite : Fragment(), Callbacks {

    private var favsList = mutableListOf<Fav?>()
    private var favs: RecyclerView? = null
    private var favsAdapter: FavoritesAdapter? = null
    private var favoritesDb: AppDatabase? = null
    private var favoritesDao: CocktailDao? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_favs, container, false)
        favoritesDb = AppDatabase.getInstance(context)
        favs = rootView.findViewById(R.id.favs_list)
        favsAdapter = FavoritesAdapter(context!!, favsList)
        refreshFavs()
        favs!!.apply {
            adapter = favsAdapter
            hasFixedSize()
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(context)
        }
        favsAdapter!!.setItems(favsList)
        return rootView
    }

    override fun onRemoveClicked(name: String?) {
        val f: Fav = (favoritesDao!!.getOne(drinkName = name))[0]!!
        favoritesDao!!.removeFromFavs(f)
        refreshFavs()
    }

    override fun onTitleFound(name: String?) {}

    private fun refreshFavs(){
        favoritesDao = favoritesDb!!.coctailDao()
        favsList = favoritesDao!!.getFavs()
        favsAdapter!!.setItems(favsList)
    }

}