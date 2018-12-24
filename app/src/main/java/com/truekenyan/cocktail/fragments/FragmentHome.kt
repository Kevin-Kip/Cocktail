package com.truekenyan.cocktail.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.activities.MainActivity
import com.truekenyan.cocktail.adapters.CocktailAdapter
import com.truekenyan.cocktail.models.CocktailModel

class FragmentHome : Fragment() {
    lateinit var progressBar: ProgressBar
    lateinit var homeList: RecyclerView
    lateinit var cocktailAdapter: CocktailAdapter
    val cocktails = mutableListOf<CocktailModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        progressBar = rootView.findViewById(R.id.progress_bar)
        homeList = rootView.findViewById(R.id.home_list)
        cocktailAdapter = CocktailAdapter(context as Context, cocktails)
        cocktailAdapter.setItems(MainActivity().cocktails)
        homeList.apply {
            adapter = cocktailAdapter
            hasFixedSize()
            layoutManager = GridLayoutManager(context, 2)
            itemAnimator = DefaultItemAnimator()
        }
        return rootView
    }
}