package com.truekenyan.cocktail.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
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
import com.truekenyan.cocktail.adapters.IngredientsAdapter
import com.truekenyan.cocktail.models.CocktailModel
import com.truekenyan.cocktail.models.Ingredient
import com.truekenyan.cocktail.utils.Commons
import kotlinx.android.synthetic.main.activity_cock_tail.*
import org.json.JSONObject

class CocktailActivity : AppCompatActivity() {

    private var ingredients = mutableListOf<Ingredient>()
    private var drinkId: String? = "13060" //TODO use fetched ID
    private lateinit var i: Intent
    private lateinit var cockTail: CocktailModel

    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var cocktailImage: ImageView
    private lateinit var methodText: TextView
    private lateinit var ingredientsRecycler: RecyclerView

    private var requestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cock_tail)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initViews()

        i = intent
        if (i.hasExtra(Commons.DRINK_ID)){
            drinkId = i.getStringExtra(Commons.DRINK_ID)
        }

        requestQueue = Volley.newRequestQueue(this@CocktailActivity)

        ingredientsAdapter = IngredientsAdapter(applicationContext,ingredients)
        ingredientsRecycler.apply {
            adapter = ingredientsAdapter
            hasFixedSize()
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(this@CocktailActivity)
        }
    }

    private fun initViews(){
        cocktailImage = findViewById(R.id.cocktail_image)
        methodText = findViewById(R.id.method)
        ingredientsRecycler = findViewById(R.id.ingredients_recycler)
    }

    private var jsonObjectRequest = JsonObjectRequest(Request.Method.GET,
            Commons.COCKTAIL+drinkId,
            JSONObject(),
            Response.Listener {
                val drinkObject: JSONObject = (it.getJSONArray(Commons.DRINKS))[0] as JSONObject
                cockTail = Gson().fromJson(drinkObject.toString(),CocktailModel::class.java)
                ingredients.clear()
                for (i in 1..15){
                    val ingredient = drinkObject.get("strIngredient$i") as String?
                    val measure = drinkObject.get("strMeasure$i") as String?

                    if (!ingredient.equals("")){
                        if (measure.equals("")){
                            ingredients.add(Ingredient(ingredient,""))
                        } else {
                            ingredients.add(Ingredient(ingredient, measure))
                        }
                    }
                }

                method.text = cockTail.strInstructions
                ingredientsAdapter.setIngredients(ingredients)
                Picasso.get().load(cockTail.strDrinkThumb).into(cocktail_image)
                collapsing_toolbar.title = cockTail.strDrink
            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, "Unable to fetch cocktail", Toast.LENGTH_SHORT).show()
                Log.d("VolleyError", it.message)
            })

    override fun onStart() {
        super.onStart()
        requestQueue!!.add(jsonObjectRequest)
    }
}
