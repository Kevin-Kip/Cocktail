package com.truekenyan.cocktail.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
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
import com.truekenyan.cocktail.callbacks.Callbacks
import com.truekenyan.cocktail.models.CocktailModel
import com.truekenyan.cocktail.utils.Commons
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class FragmentHome : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var homeList: RecyclerView
    private lateinit var cocktailAdapter: CocktailAdapter
    private var cocktails = mutableListOf<CocktailModel>()
    private var requestQueue: RequestQueue? = null
    private var listener: Callbacks? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        progressBar = rootView.findViewById(R.id.progress_bar)
        homeList = rootView.findViewById(R.id.home_list)
        cocktailAdapter = CocktailAdapter(context!!, cocktails, true)
        requestQueue = Volley.newRequestQueue(context)

        setHasOptionsMenu(true)

        if((Random().nextInt() % 2) == 0){
            fetchDrinks(Commons.URL_ALCOHOLIC)
            listener!!.onTitleFound(Commons.ALCOHOLIC)
        } else {
            fetchDrinks(Commons.URL_NON_ALCOHOLIC)
            listener!!.onTitleFound(Commons.NON_ALCOHOLIC)
        }

        homeList.apply {
            adapter = cocktailAdapter
            hasFixedSize()
            layoutManager = GridLayoutManager(context, 2)
            itemAnimator = DefaultItemAnimator()
        }
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.main_options, menu)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as Callbacks
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
                    cocktails = cocktails.shuffled() as MutableList<CocktailModel>
                    cocktailAdapter.setItems(cocktails)
                },
                Response.ErrorListener {
                    Toast.makeText(context, "Oooops. Unable to fetch drinks", Toast.LENGTH_SHORT).show()
                    Log.e("FETCHING", it.message)
                })

        requestQueue!!.add(jsonObjectRequest)
    }
}