package com.truekenyan.cocktail.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
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

class FragmentSearch : Fragment() {

    private var searchList: RecyclerView? = null
    private var searchInput: EditText? = null
    private var clearButton: ImageButton? = null
    private var searchButton: ImageButton? = null
    private var progressBar: ProgressBar? = null
    private var keyWord: String? = null
    private var noResults: TextView? = null

    private lateinit var cocktailAdapter: CocktailAdapter
    private var cocktails = mutableListOf<CocktailModel>()
    private var requestQueue: RequestQueue? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)
        searchList = rootView.findViewById(R.id.search_list)
        searchInput = rootView.findViewById(R.id.input_word)
        clearButton = rootView.findViewById(R.id.button_clear)
        searchButton = rootView.findViewById(R.id.button_search)
        progressBar = rootView.findViewById(R.id.progress_bar)
        noResults = rootView.findViewById(R.id.no_results)

        cocktailAdapter = CocktailAdapter(context!!, cocktails, true)
        requestQueue = Volley.newRequestQueue(context)
        searchInput!!.addTextChangedListener(searchListener)

        searchList!!.apply {
            adapter = cocktailAdapter
            hasFixedSize()
            layoutManager = GridLayoutManager(context, 2)
            itemAnimator = DefaultItemAnimator()
        }

        searchButton!!.setOnClickListener {
            keyWord = searchInput!!.text.trim().toString()
            searchButton!!.isEnabled = false
            progressBar!!.visibility = View.VISIBLE
            fetchDrinks(Commons.SEARCH_URL + keyWord)
        }

        clearButton!!.setOnClickListener {
            searchInput!!.text.clear()
        }

        return rootView
    }

    private val searchListener = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            noResults!!.visibility = View.GONE
        }

        override fun afterTextChanged(s: Editable?) {
            keyWord = s?.trim().toString()
            when (keyWord!!.length){
                0 -> {
                    clearButton!!.visibility = View.GONE
                    searchButton!!.isEnabled = false
                }
                else -> {
                    clearButton!!.visibility = View.VISIBLE
                    searchButton!!.isEnabled = true
                }
            }
        }
    }

    private fun fetchDrinks(drinkUrl: String?){
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,
                drinkUrl,
                JSONObject(),
                Response.Listener {
                    progressBar!!.visibility = View.GONE

                    if(it.has(Commons.DRINKS)) {
                        val jsonArray = it.getJSONArray(Commons.DRINKS) as JSONArray
                        if (jsonArray.length() < 1) {
                            noResults!!.visibility = View.VISIBLE
                            searchList!!.visibility = View.GONE
                            return@Listener
                        } else {
                            searchList!!.visibility = View.VISIBLE
                            for (i in 0 until (jsonArray.length() - 1)) {
                                val o = jsonArray[i] as JSONObject
                                val cockTail = Gson().fromJson(o.toString(), CocktailModel::class.java)
                                if (!cocktails.contains(cockTail)) {
                                    cocktails.add(cockTail)
                                }
                            }
                        }
                        cocktails = cocktails.shuffled() as MutableList<CocktailModel>
                        cocktailAdapter.setItems(cocktails)
                    } else {
                        noResults!!.visibility = View.VISIBLE
                        searchList!!.visibility = View.GONE
                    }
                },
                Response.ErrorListener {
                    progressBar!!.visibility = View.GONE
                    noResults!!.visibility = View.VISIBLE
                    searchList!!.visibility = View.GONE
                    Log.e("FETCHING", it.message)
                })

        requestQueue!!.add(jsonObjectRequest)
    }
}