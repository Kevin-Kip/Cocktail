package com.truekenyan.cocktail.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.fragments.FragmentFavorite
import com.truekenyan.cocktail.fragments.FragmentHome
import com.truekenyan.cocktail.fragments.FragmentSearch
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayout: LinearLayout
    private var isHome = true

    private var homeFragment: Fragment? = null
    private var searchFragment: Fragment? = null
    private var favoritesFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        linearLayout = findViewById(R.id.message)

        homeFragment = FragmentHome()
        searchFragment = FragmentSearch()
        favoritesFragment = FragmentFavorite()

        changeFragment(homeFragment!!)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragment(homeFragment!!)
                isHome = true
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                changeFragment(searchFragment!!)
                isHome = false
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                changeFragment(favoritesFragment!!)
                isHome = false
                return@OnNavigationItemSelectedListener true
            }
            else -> {
                changeFragment(homeFragment!!)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId){
            R.id.options_filter -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
