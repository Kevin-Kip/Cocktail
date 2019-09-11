package com.truekenyan.cocktail.ui.fragments

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
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
import com.revosleap.simpleadapter.SimpleAdapter
import com.revosleap.simpleadapter.SimpleCallbacks
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.models.CocktailModel
import com.truekenyan.cocktail.utils.Commons
import com.truekenyan.cocktail.utils.NetManager
import com.truekenyan.cocktail.utils.PrefManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class FragmentSearch : androidx.fragment.app.Fragment() {

    private var searchList: androidx.recyclerview.widget.RecyclerView? = null
    private var searchInput: EditText? = null
    private var clearButton: ImageButton? = null
    private var searchButton: ImageButton? = null
    private var progressBar: ProgressBar? = null
    private var keyWord: String? = null
    private var noResults: TextView? = null
    private var searchByName: Boolean? = null
    private lateinit var cocktailAdapter: SimpleAdapter
    private var cocktails = mutableListOf<CocktailModel>()
    private var requestQueue: RequestQueue? = null
    private var prefManager: PrefManager? = null
    private val simpleCallbacks: SimpleCallbacks = object : SimpleCallbacks {
        override fun bindView(view: View, item: Any, position: Int) {

        }

        override fun onViewClicked(view: View, item: Any, position: Int) {

        }

        override fun onViewLongClicked(it: View?, item: Any, position: Int) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)
        initViews(rootView)
        setHasOptionsMenu(true)
        searchByName = prefManager!!.searchByName()
        cocktailAdapter = SimpleAdapter(R.layout.item_home_list, simpleCallbacks)
        requestQueue = Volley.newRequestQueue(context)
        searchInput!!.addTextChangedListener(searchListener)

        searchList!!.apply {
            adapter = cocktailAdapter
            hasFixedSize()
            layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 2)
            itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        }
        return rootView
    }

    private fun initViews(rootView: View){
        searchList = rootView.findViewById(R.id.search_list)
        searchInput = rootView.findViewById(R.id.input_word)
        clearButton = rootView.findViewById(R.id.button_clear)
        searchButton = rootView.findViewById(R.id.button_search)
        progressBar = rootView.findViewById(R.id.progress_bar)
        noResults = rootView.findViewById(R.id.no_results)
        prefManager = PrefManager(context!!)

        searchInput!!.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)v.isEnabled
        }

        searchButton!!.setOnClickListener {
            keyWord = searchInput!!.text.trim().toString()
            keyWord = keyWord!!.replace(" ", "_")
            searchButton!!.isEnabled = false
            progressBar!!.visibility = View.VISIBLE
        }

        clearButton!!.setOnClickListener {
            searchInput!!.text.clear()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val menuIngredient = menu?.findItem(R.id.search_ingredient)
        val menuName = menu?.findItem(R.id.search_name)
        when {
            prefManager!!.searchByName() -> menuName!!.isChecked = true
            else -> menuIngredient!!.isChecked = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.search_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId){
            R.id.search_ingredient -> {
                if (prefManager!!.searchByName()) {
                    prefManager!!.setSearchByName(false)
                    if (keyWord != null) fetchDrinks(Commons.INGREDIENT_SEARCH + keyWord)
                }
                return true
            }
            R.id.search_name -> {
                if (!prefManager!!.searchByName()) {
                    prefManager!!.setSearchByName(true)
                    if (keyWord != null) fetchDrinks(Commons.NAME_SEARCH + keyWord)
                }
                return true
            }
        }
        activity!!.invalidateOptionsMenu()
        return super.onOptionsItemSelected(item)
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
        keyWord = null
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
                                if (!cocktails.contains(cockTail)) cocktails.add(cockTail)
                            }
                        }
                        cocktails = cocktails.shuffled() as MutableList<CocktailModel>
                        cocktailAdapter.addManyItems(cocktails as MutableList<Any>)
                    } else {
                        noResults!!.visibility = View.VISIBLE
                        searchList!!.visibility = View.GONE
                    }
                },
                Response.ErrorListener {
                    progressBar!!.visibility = View.GONE
                    noResults!!.visibility = View.VISIBLE
                    searchList!!.visibility = View.GONE
                    Log.e("FETCHING", it!!.message)
                })

        if (NetManager.isConnected(context!!) && NetManager.isConnectedFast(context!!)) {
            requestQueue!!.add(jsonObjectRequest)
        } else {
            Snackbar.make(container, "Network connection is slow", Snackbar.LENGTH_INDEFINITE)
                    .setAction("DISMISS"){}
                    .show()
        }
    }
}