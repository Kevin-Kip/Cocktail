package com.truekenyan.cocktail.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.adapters.CocktailAdapter
import com.truekenyan.cocktail.models.CocktailModel
import com.truekenyan.cocktail.utils.Commons
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class FragmentHome : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var homeList: RecyclerView
    private lateinit var cocktailAdapter: CocktailAdapter
    private val cocktails = mutableListOf<CocktailModel>()
    private var requestQueue: RequestQueue? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        progressBar = rootView.findViewById(R.id.progress_bar)
        homeList = rootView.findViewById(R.id.home_list)
        cocktailAdapter = CocktailAdapter(context!!, cocktails)
        requestQueue = Volley.newRequestQueue(context)

        if((Random().nextInt() % 2) == 0){
            fetchDrinks(Commons.URL_ALCOHOLIC)
        } else {
            fetchDrinks(Commons.URL_NON_ALCOHOLIC)
        }

        homeList.apply {
            adapter = cocktailAdapter
            hasFixedSize()
            layoutManager = GridLayoutManager(context, 2)
            itemAnimator = DefaultItemAnimator()
        }
        return rootView
    }

    private fun fetchDrinks(drinkUrl: String?){
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,
                drinkUrl,
                JSONObject(),
                Response.Listener {
                    progressBar.visibility = View.GONE
                    homeList.visibility = View.VISIBLE
                    val jsonArray = it.getJSONArray(Commons.DRINKS) as JSONArray
                    for (i in 0 until (jsonArray.length() - 1)){
                        val o = jsonArray[i] as JSONObject
                        val cockTail = Gson().fromJson(o.toString(), CocktailModel::class.java)
                        if (!cocktails.contains(cockTail)) {
                            cocktails.add(cockTail)
                        }
                    }
                    cocktailAdapter.setItems(cocktails)
                },
                Response.ErrorListener {
                    Toast.makeText(context, "Oooops. Unable to fetch drinks", Toast.LENGTH_SHORT).show()
                    Log.e("FETCHING", it.message)
                })

        requestQueue!!.add(jsonObjectRequest)
    }
}