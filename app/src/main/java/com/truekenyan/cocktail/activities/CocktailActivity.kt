package com.truekenyan.cocktail.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.adapters.CocktailAdapter
import com.truekenyan.cocktail.adapters.IngredientsAdapter
import com.truekenyan.cocktail.callbacks.CocktailDao
import com.truekenyan.cocktail.database.AppDatabase
import com.truekenyan.cocktail.models.CocktailModel
import com.truekenyan.cocktail.models.Fav
import com.truekenyan.cocktail.models.Ingredient
import com.truekenyan.cocktail.utils.Commons
import kotlinx.android.synthetic.main.activity_cock_tail.*
import org.json.JSONObject
import java.util.*

class CocktailActivity : AppCompatActivity() {

    private var ingredients = mutableListOf<Ingredient>()
    private var suggestions = mutableListOf<CocktailModel>()
    private var drinkId: Int? = null
    private var i: Intent? = null
    private var cockTail: CocktailModel? = null
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var cocktailAdapter: CocktailAdapter
    private lateinit var cocktailImage: ImageView
    private lateinit var methodText: TextView
    private lateinit var ingredientsRecycler: RecyclerView
    private lateinit var moreRecyclerView: RecyclerView
    private var requestQueue: RequestQueue? = null
    private var favoriteImage: ImageView? = null
    private var favorites = mutableListOf<Fav?>()
    private var favoritesDb: AppDatabase? = null
    private var favoritesDao: CocktailDao? = null
    private var isFavorite: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cock_tail)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initViews()

        i = intent
        drinkId = (i!!.getStringExtra(Commons.DRINK_ID)).toInt()
        val fullURL: String = Commons.COCKTAIL + drinkId

        favoritesDb = AppDatabase.getInstance(context = applicationContext)
        favoritesDao = favoritesDb!!.coctailDao()

        requestQueue = Volley.newRequestQueue(this@CocktailActivity)
        getDetails(fullURL)

        ingredientsAdapter = IngredientsAdapter(this@CocktailActivity,ingredients)
        cocktailAdapter = CocktailAdapter(this@CocktailActivity, suggestions, false)
        ingredientsRecycler.apply {
            adapter = ingredientsAdapter
            hasFixedSize()
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(this@CocktailActivity)
        }
        moreRecyclerView.apply {
            adapter = cocktailAdapter
            hasFixedSize()
            itemAnimator = DefaultItemAnimator()
            layoutManager = GridLayoutManager(context,1, GridLayoutManager.HORIZONTAL, false)
        }

        favoriteImage!!.setOnClickListener {
            if (isFavorite!!){
                isFavorite = false
//                favoritesDao!!.removeFromFavs(fav) TODO REMOVE FROM FAVORITES
                return@setOnClickListener
            }
            val fav = Fav(favId = favorites.size + 1,
                    drinkId = drinkId!!.toString(),
                    drinkName = cockTail!!.strDrink,
                    drinkPhoto = cockTail!!.strDrinkThumb)
            favoritesDao!!.addToFavs(fav)
            favoriteImage!!.setImageResource(R.drawable.ic_favorite_selected)
            isFavorite = true
        }

        favorites = favoritesDao!!.getFavs()
    }

    private fun initViews(){
        cocktailImage = findViewById(R.id.cocktail_image)
        methodText = findViewById(R.id.method)
        ingredientsRecycler = findViewById(R.id.ingredients_recycler)
        moreRecyclerView = findViewById(R.id.more_list)
        favoriteImage = findViewById(R.id.favorite)
        favoriteImage!!.isEnabled = false
    }

    private fun getDetails(url: String) {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,
                url,
                JSONObject(),
                Response.Listener {
                    val drinkObject: JSONObject = (it.getJSONArray(Commons.DRINKS))[0] as JSONObject
                    cockTail = Gson().fromJson(drinkObject.toString(), CocktailModel::class.java)
                    ingredients.clear()
                    for (i in 1..15) {
                        val ingredient = drinkObject.get("strIngredient$i") as String?
                        val measure = drinkObject.get("strMeasure$i") as String?

                        if (!ingredient.equals("")) {
                            if (measure.equals("")) {
                                ingredients.add(Ingredient(ingredient, ""))
                            } else {
                                ingredients.add(Ingredient(ingredient, measure))
                            }
                        }
                    }

                    method.text = (cockTail!!.strInstructions)!!.replace(". ", ".\n")
                    ingredientsAdapter.setIngredients(ingredients)
                    Picasso.get()
                            .load(cockTail!!.strDrinkThumb)
                            .placeholder(R.drawable.placeholder)
                            .into(cocktail_image)
                    collapsing_toolbar.title = cockTail!!.strDrink
                    favoriteImage!!.isEnabled = true
                    tag.text = cockTail!!.strAlcoholic
                    for (fav in favorites){
                        if (fav!!.drinkId == (cockTail!!.idDrink)){
                            favoriteImage!!.setImageResource(R.drawable.ic_favorite_selected)
                            isFavorite = true
                            break
                        }
                    }
                    if (i!!.hasExtra(Commons.COCKTAILS)) {
                        getMore(drinkId.toString(), i!!.getParcelableArrayListExtra(Commons.COCKTAILS))
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(applicationContext, "Unable to fetch cocktail", Toast.LENGTH_SHORT).show()
                    Log.d("UNABLE TO FETCH", it.message)
                })
        requestQueue!!.add(jsonObjectRequest)
    }

    private fun getMore(currentId: String?, list: ArrayList<CocktailModel>){
        val newList = mutableListOf<CocktailModel>()
        for (i in 0..12) {
            for (item in list) {
                if (item.idDrink != currentId) {
                    newList.add(item)
                }
            }
        }
        cocktailAdapter.setItems(newList)
    }
}
