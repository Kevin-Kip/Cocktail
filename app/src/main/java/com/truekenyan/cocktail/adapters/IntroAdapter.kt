package com.truekenyan.cocktail.adapters

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class IntroAdapter(private val screens: MutableList<Int>, private val context: Context) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(screens[position], container, false)
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, any: Any): Boolean = (view == any)

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as View)
    }

    override fun getCount(): Int = screens.size
}