package com.truekenyan.cocktail.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.revosleap.simpleadapter.SimpleAdapter
import com.revosleap.simpleadapter.SimpleCallbacks
import com.squareup.picasso.Picasso
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.callbacks.Callbacks
import com.truekenyan.cocktail.callbacks.CocktailDao
import com.truekenyan.cocktail.database.AppDatabase
import com.truekenyan.cocktail.models.Fav
import com.truekenyan.cocktail.ui.activities.CocktailActivity
import com.truekenyan.cocktail.utils.Commons
import kotlinx.android.synthetic.main.item_fav.view.*

class FragmentFavorite : Fragment(), Callbacks {

    private var favsList = mutableListOf<Any>()
    private var favs: RecyclerView? = null
    private var favsAdapter: SimpleAdapter? = null
    private var favoritesDb: AppDatabase? = null
    private var favoritesDao: CocktailDao? = null
    private val callbacks = object : SimpleCallbacks {
        override fun bindView(view: View, item: Any, position: Int) {
            item as Fav
            val drinkImage = view.fav_image
            val drinkName = view.fav_name

            Picasso.get()
                    .load(item.drinkPhoto)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(drinkImage)
            drinkName.text = item.drinkName

            (view.remove).setOnClickListener(removeItem(item))
        }

        override fun onViewClicked(view: View, item: Any, position: Int) {
            item as Fav
            val i = Intent(context, CocktailActivity::class.java).apply {
                putExtra(Commons.DRINK_ID, item.drinkId)
            }
            context!!.startActivity(i)
        }

        override fun onViewLongClicked(it: View?, item: Any, position: Int) {
//TODO remove item dialog
        }
    }

    private fun removeItem(item: Fav): View.OnClickListener? {
        return View.OnClickListener {
            favoritesDao!!.removeFromFavs(item)
            refreshFavs()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_favs, container, false)
        favoritesDb = AppDatabase.getInstance(context)
        favs = rootView.findViewById(R.id.favs_list)
        favsAdapter = SimpleAdapter(R.layout.item_fav, callbacks)
        setHasOptionsMenu(true)
        refreshFavs()
        favs!!.apply {
            adapter = favsAdapter
            hasFixedSize()
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(context)
        }
        favsAdapter!!.changeItems(favsList)
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
        favsList = favoritesDao!!.getFavs() as MutableList<Any>
        favsAdapter!!.changeItems(favsList)
    }
}