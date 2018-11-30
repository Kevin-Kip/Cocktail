package com.truekenyan.cocktail.activities

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.util.LruCache
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.models.CocktailModel
import com.truekenyan.cocktail.models.Ingredient
import com.truekenyan.cocktail.utils.Commons
import org.json.JSONObject

class CocktailActivity : AppCompatActivity() {

    private var ingredients = mutableListOf<Ingredient>()
    private var drinkId = 0
    private lateinit var i: Intent
    private lateinit var cockTail: CocktailModel
    private val requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)
    private val imageCache: ImageLoader.ImageCache = object : ImageLoader.ImageCache{
        private val cache: LruCache<String, Bitmap> = LruCache(30)
        override fun getBitmap(url: String?): Bitmap {
            return cache.get(url as String) as Bitmap
        }

        override fun putBitmap(url: String?, bitmap: Bitmap?) {
            cache.put(url as String, bitmap as Bitmap)
        }
    }
    private val imageLoader: ImageLoader = ImageLoader(requestQueue, imageCache)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cock_tail)

        i = intent
        if (i.hasExtra(Commons.DRINK_ID)){
            drinkId = i.getIntExtra(Commons.DRINK_ID, 0)
        }
    }

    private var jsonObjectRequest = JsonObjectRequest(Request.Method.GET,
            Commons.COCKTAIL+drinkId,
            JSONObject(),
            Response.Listener {
                val drinkObject: JSONObject = (it.getJSONArray("drinks"))[0] as JSONObject
                cockTail = Gson().fromJson(drinkObject.toString(),CocktailModel::class.java)

                for (i in 1..15){
                    val ingredient = drinkObject.get("strIngredient$i") as String?
                    val measure = drinkObject.get("strMeasure$i") as String?

                    if (!ingredient.isNullOrEmpty()){
                        if (measure.isNullOrEmpty()){
                            ingredients.add(Ingredient(ingredient,""))
                        } else {
                            ingredients.add(Ingredient(ingredient, measure))
                        }
                    }
                }
            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, "Unable to fetch cocktail", Toast.LENGTH_SHORT).show()
                Log.d("VolleyError", it.message)
            })

    override fun onStart() {
        super.onStart()
        requestQueue.add(jsonObjectRequest)
    }
}
