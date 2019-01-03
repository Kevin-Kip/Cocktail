package com.truekenyan.cocktail.adapters

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class IntroAdapter(val screens: MutableList<Int>, private val context: Context): PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(screens[position], container, false)
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, any: Any): Boolean = (view == any)

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        super.destroyItem(container, position, any)
        val view = any as View
        container.removeView(view)
    }

    override fun getCount(): Int = screens.size
}