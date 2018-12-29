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
import com.truekenyan.cocktail.utils.PrefManager
import org.json.JSONArray
import org.json.JSONObject

class FragmentHome : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var homeList: RecyclerView
    private lateinit var cocktailAdapter: CocktailAdapter
    private var cocktails = mutableListOf<CocktailModel>()
    private var requestQueue: RequestQueue? = null
    private var listener: Callbacks? = null
    private var prefManager: PrefManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        progressBar = rootView.findViewById(R.id.progress_bar)
        homeList = rootView.findViewById(R.id.home_list)
        cocktailAdapter = CocktailAdapter(context!!, cocktails, true)
        requestQueue = Volley.newRequestQueue(context)
        prefManager = PrefManager(context!!.applicationContext)

        setHasOptionsMenu(true)
        fetchDrinks()

        homeList.apply {
            adapter = cocktailAdapter
            hasFixedSize()
            layoutManager = GridLayoutManager(context, 2)
            itemAnimator = DefaultItemAnimator()
        }
        return rootView
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val randomItem = menu?.findItem(R.id.random)
        val ascItem = menu?.findItem(R.id.ascending)
        val descItem = menu?.findItem(R.id.descending)
        when {
            prefManager!!.getOrder() == Commons.RANDOM -> randomItem!!.isChecked = true
            prefManager!!.getOrder() == Commons.ASCENDING -> ascItem!!.isChecked = true
            prefManager!!.getOrder() == Commons.DESCENDING -> descItem!!.isChecked = true
        }

        val alcoholic = menu?.findItem(R.id.alcoholic)
        val nonAlcoholic = menu?.findItem(R.id.non_alcoholic)
        when (prefManager!!.isAlcoholic()){
            true -> alcoholic!!.isChecked = true
            else -> nonAlcoholic!!.isChecked = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.home_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId){
            R.id.alcoholic -> {
                prefManager!!.setAlcoholic(true)
                fetchDrinks()
            }
            R.id.non_alcoholic -> {
                prefManager!!.setAlcoholic(false)
                fetchDrinks()
            }
            R.id.ascending -> {
                prefManager!!.setOrder(Commons.ASCENDING)
                sortList()
            }
            R.id.descending -> {
                prefManager!!.setOrder(Commons.DESCENDING)
                sortList()
            }
            R.id.random -> {
                prefManager!!.setOrder(Commons.RANDOM)
                sortList()
            }
        }
        activity!!.invalidateOptionsMenu()
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as Callbacks
    }

    private fun getTitle(){
        when {
            prefManager!!.isAlcoholic() -> listener!!.onTitleFound(Commons.ALCOHOLIC)
            else -> listener!!.onTitleFound(Commons.NON_ALCOHOLIC)
        }
    }

    private fun fetchDrinks(){
        val drinkUrl: String? = when(prefManager!!.isAlcoholic()) {
            true -> Commons.URL_ALCOHOLIC
            else -> Commons.URL_NON_ALCOHOLIC
        }

        progressBar.visibility = View.VISIBLE
        homeList.visibility = View.GONE
        cocktails.clear()
        getTitle()

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
                    sortList()
                },
                Response.ErrorListener {
                    Toast.makeText(context, "Oooops. Unable to fetch drinks", Toast.LENGTH_SHORT).show()
                    Log.e("FETCHING", it.message)
                })

        requestQueue!!.add(jsonObjectRequest)
    }

    private fun sortList(){
        when {
            prefManager!!.getOrder() == Commons.ASCENDING -> cocktails.sortBy { it.strDrink }
            prefManager!!.getOrder() == Commons.DESCENDING -> cocktails.sortByDescending {it.strDrink}
            else -> cocktails = cocktails.shuffled() as MutableList<CocktailModel>
        }
        cocktailAdapter.setItems(cocktails)
    }
}