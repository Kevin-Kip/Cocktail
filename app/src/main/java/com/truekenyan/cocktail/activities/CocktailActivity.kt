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
import com.truekenyan.cocktail.models.CocktailModel
import com.truekenyan.cocktail.models.Ingredient
import com.truekenyan.cocktail.utils.Commons
import kotlinx.android.synthetic.main.activity_cock_tail.*
import org.json.JSONArray
import org.json.JSONObject

class CocktailActivity : AppCompatActivity() {

    private var ingredients = mutableListOf<Ingredient>()
    private var suggestions = mutableListOf<CocktailModel>()
    private var drinkId: Int? = null
    private lateinit var i: Intent
    private lateinit var cockTail: CocktailModel
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var cocktailAdapter: CocktailAdapter
    private lateinit var cocktailImage: ImageView
    private lateinit var methodText: TextView
    private lateinit var ingredientsRecycler: RecyclerView
    private lateinit var moreRecyclerView: RecyclerView
    private var requestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cock_tail)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initViews()

        i = intent
        drinkId = (i.getStringExtra(Commons.DRINK_ID)).toInt()
        val fullURL: String = Commons.COCKTAIL + drinkId
        Toast.makeText(applicationContext, "URL is $fullURL", Toast.LENGTH_LONG).show()

        requestQueue = Volley.newRequestQueue(this@CocktailActivity)
        getDetails(fullURL)

        ingredientsAdapter = IngredientsAdapter(this@CocktailActivity,ingredients)
        cocktailAdapter = CocktailAdapter(this@CocktailActivity, suggestions)
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
    }

    private fun initViews(){
        cocktailImage = findViewById(R.id.cocktail_image)
        methodText = findViewById(R.id.method)
        ingredientsRecycler = findViewById(R.id.ingredients_recycler)
        moreRecyclerView = findViewById(R.id.more_list)
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

                    method.text = (cockTail.strInstructions)!!.replace(". ", ".\n")
                    ingredientsAdapter.setIngredients(ingredients)
                    Picasso.get().load(cockTail.strDrinkThumb).into(cocktail_image)
                    collapsing_toolbar.title = cockTail.strDrink
                },
                Response.ErrorListener {
                    Toast.makeText(applicationContext, "Unable to fetch cocktail", Toast.LENGTH_SHORT).show()
                    Log.d("UNABLE TO FETCH", it.message)
                })
        requestQueue!!.add(jsonObjectRequest)
    }

    private fun getMore(suggestion: String?){
        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                Commons.SEARCH_URL+suggestion,
                JSONObject(),
                Response.Listener {
                    val jsonArray = it.getJSONArray(Commons.DRINKS) as JSONArray
                    for (i in 0..10){
                        val o = jsonArray[i] as JSONObject
                        val cockTail = Gson().fromJson(o.toString(), CocktailModel::class.java)
                        if (!suggestions.contains(cockTail)) {
                            suggestions.add(cockTail)
                        }
                    }
                    cocktailAdapter.setItems(suggestions)
                },
                Response.ErrorListener {
                    Toast.makeText(this@CocktailActivity, "Oooops. Unable to fetch drinks", Toast.LENGTH_SHORT).show()
                    Log.e("FETCHING", it.message)
                })
        requestQueue!!.add(jsonObjectRequest)
    }
}
