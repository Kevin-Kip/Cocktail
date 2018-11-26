package com.truekenyan.cocktail.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.models.CocktailModel
import com.truekenyan.cocktail.utils.Commons
import org.json.JSONObject

class CockTailActivity : AppCompatActivity() {

    private var drinkId = 0
    private lateinit var i: Intent
    private lateinit var cockTail: CocktailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cock_tail)

        i = intent
        if (i.hasExtra(Commons.DRINK_ID)){
            drinkId = i.getIntExtra(Commons.DRINK_ID, 0)
        }
    }

    var jsonObjectRequest = JsonObjectRequest(Request.Method.GET,
            Commons.COCKTAIL,
            JSONObject(),
            Response.Listener {

            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, "Unable to fetch cocktail", Toast.LENGTH_SHORT).show()
                Log.d("VolleyError", it.message)
            })
}
