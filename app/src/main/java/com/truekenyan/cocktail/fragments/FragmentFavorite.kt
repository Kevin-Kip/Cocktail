package com.truekenyan.cocktail.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
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
        setHasOptionsMenu(true)
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

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val clearItem: MenuItem? = menu!!.findItem(R.id.clear_favorites)
        if (favsList.isEmpty()){
            clearItem!!.isVisible = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.favorite_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId){
            R.id.clear_favorites -> {
                favoritesDao!!.clearFavorites()
                return true
            }
        }
        activity!!.invalidateOptionsMenu()
        return super.onOptionsItemSelected(item)
    }

    override fun onRemoveClicked(name: String?) {
        val f: Fav = (favoritesDao!!.getOne(drinkName = name))[0]!!
        Toast.makeText(context, "Removing", Toast.LENGTH_SHORT).show()
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