package com.truekenyan.cocktail.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.fragments.FragmentFavorite
import com.truekenyan.cocktail.fragments.FragmentHome
import com.truekenyan.cocktail.fragments.FragmentSearch
import com.truekenyan.cocktail.models.CocktailModel
import com.truekenyan.cocktail.utils.Commons
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayout: LinearLayout
    private var isHome = true

    private var requestQueue: RequestQueue? = null
    var cocktails = mutableListOf<CocktailModel>()

    var homeFragment: FragmentHome? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        linearLayout = findViewById(R.id.message)

        homeFragment = FragmentHome()

        changeFragment(homeFragment!!)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        requestQueue = Volley.newRequestQueue(this@MainActivity)

        if((Random().nextInt() % 2) == 0){
            fetchDrinks(Commons.URL_ALCOHOLIC)
        } else {
            fetchDrinks(Commons.URL_NON_ALCOHOLIC)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isHome) {
            menuInflater.inflate(R.menu.main_options, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.options_filter){

        }
        return true
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragment(FragmentHome())
                isHome = true
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                changeFragment(FragmentSearch())
                isHome = false
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                changeFragment(FragmentFavorite())
                isHome = false
                return@OnNavigationItemSelectedListener true
            }
            else -> {
                changeFragment(FragmentHome())
                isHome = true
                return@OnNavigationItemSelectedListener true
            }
        }
    }

    private fun changeFragment(fragment: Fragment){
        val manager = supportFragmentManager
        manager.popBackStack()
        manager.beginTransaction()
                .addToBackStack(fragment.tag)
                .add(R.id.message, fragment)
                .commit()
    }

    private fun  fetchDrinks(drinkUrl: String?){
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,
                drinkUrl,
                JSONObject(),
                Response.Listener {
                    cocktails.clear()
                    val jsonArray = it.getJSONArray(Commons.DRINKS) as JSONArray
                    for (i in 0 until (jsonArray.length() - 1)){
                        val o = jsonArray[i] as JSONObject
                        val cockTail = Gson().fromJson(o.toString(), CocktailModel::class.java)
                        cocktails.add(cockTail)
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(this@MainActivity, "Oooops. Unable to fetch drinks", Toast.LENGTH_SHORT).show()
                    Log.e("FETCHING", it.message)
                })

        requestQueue!!.add(jsonObjectRequest)
    }
}
