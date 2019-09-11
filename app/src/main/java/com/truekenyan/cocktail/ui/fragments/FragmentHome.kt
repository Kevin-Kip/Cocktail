package com.truekenyan.cocktail.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.revosleap.simpleadapter.SimpleAdapter
import com.revosleap.simpleadapter.SimpleCallbacks
import com.squareup.picasso.Picasso
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.callbacks.Callbacks
import com.truekenyan.cocktail.models.CocktailModel
import com.truekenyan.cocktail.request.APIClient
import com.truekenyan.cocktail.request.APIService
import com.truekenyan.cocktail.ui.activities.CocktailActivity
import com.truekenyan.cocktail.utils.Commons
import com.truekenyan.cocktail.utils.PrefManager
import kotlinx.android.synthetic.main.item_home_list.view.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class FragmentHome : Fragment() {

    private var apiService: APIService? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var homeList: RecyclerView
    private lateinit var cocktailAdapter: SimpleAdapter
    private var cocktails = mutableListOf<CocktailModel>()
    private var requestQueue: RequestQueue? = null
    private var listener: Callbacks? = null
    private var prefManager: PrefManager? = null
    private val simpleCallbacks: SimpleCallbacks = object : SimpleCallbacks {
        override fun bindView(view: View, item: Any, position: Int) {
            item as CocktailModel

            val imageView = view.cocktail_image
            val cocktailName = view.cocktail_name
            Picasso.get()
                    .load(item.strDrinkThumb)
                    .fit()
                    .placeholder(R.drawable.placeholder)
                    .into(imageView)
            cocktailName.text = item.strDrink
        }

        override fun onViewClicked(view: View, item: Any, position: Int) {
            item as CocktailModel
            val i = Intent(context, CocktailActivity::class.java).apply { putExtra(Commons.DRINK_ID, item.idDrink) }
            context?.startActivity(i)
        }

        override fun onViewLongClicked(it: View?, item: Any, position: Int) {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        progressBar = rootView.findViewById(R.id.progress_bar)
        homeList = rootView.findViewById(R.id.home_list)
        cocktailAdapter = SimpleAdapter(R.layout.item_home_list, simpleCallbacks)
        requestQueue = Volley.newRequestQueue(context)
        prefManager = PrefManager(context!!.applicationContext)

        apiService = APIClient.getAPIService()

        setHasOptionsMenu(true)
        fetchDrinks()

        homeList.apply {
            adapter = cocktailAdapter
            hasFixedSize()
            layoutManager = GridLayoutManager(context, 3)
            itemAnimator = DefaultItemAnimator()
        }
        return rootView
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val randomItem = menu.findItem(R.id.random)
        val ascItem = menu.findItem(R.id.ascending)
        val descItem = menu.findItem(R.id.descending)
        when (prefManager?.getOrder()) {
            Commons.RANDOM -> randomItem?.isChecked = true
            Commons.ASCENDING -> ascItem?.isChecked = true
            Commons.DESCENDING -> descItem?.isChecked = true
        }

        val alcoholic = menu.findItem(R.id.alcoholic)
        val nonAlcoholic = menu.findItem(R.id.non_alcoholic)
        when (prefManager?.isAlcoholic()) {
            true -> alcoholic?.isChecked = true
            else -> nonAlcoholic?.isChecked = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
        activity?.invalidateOptionsMenu()
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as Callbacks
    }

    private fun getTitle() {
        when {
            prefManager!!.isAlcoholic() -> listener!!.onTitleFound(Commons.ALCOHOLIC)
            else -> listener!!.onTitleFound(Commons.NON_ALCOHOLIC)
        }
    }

    private fun fetchDrinks() {
        val request: Call<Any> = if (prefManager!!.isAlcoholic())
            apiService?.getAlcoholic()!!
        else apiService?.getNonAlcoholic()!!
        progressBar.visibility = View.VISIBLE
        homeList.visibility = View.GONE
        cocktails.clear()
        getTitle()

        request.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: retrofit2.Response<Any>) {
                progressBar.visibility = View.GONE
                homeList.visibility = View.VISIBLE
                val result: Any =response.body()!!
                parseJson(result)
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.e("ERROR", t.message!!)
                Toast.makeText(context, "Oooops. Unable to fetch drinks", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun parseJson(result: Any?) {
        val res = Gson().toJson(result)
        val response = JSONObject(res)
        val jsonArray = response.getJSONArray(Commons.DRINKS) as JSONArray
        for (i in 0 until (jsonArray.length() - 1)) {
            val o = jsonArray[i] as JSONObject
            val cockTail = Gson().fromJson(o.toString(), CocktailModel::class.java)
            if (!cocktails.contains(cockTail)) cocktails.add(cockTail)
        }
        sortList()
    }

    private fun sortList() {
        when (prefManager?.getOrder()) {
            Commons.ASCENDING -> cocktails.sortBy { it.strDrink }
            Commons.DESCENDING -> cocktails.sortByDescending { it.strDrink }
            else -> cocktails = cocktails.shuffled() as MutableList<CocktailModel>
        }
        cocktailAdapter.changeItems(cocktails as MutableList<Any>)
    }
}