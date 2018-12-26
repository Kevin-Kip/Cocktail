package com.truekenyan.cocktail.activities

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.callbacks.Callbacks
import com.truekenyan.cocktail.fragments.FragmentFavorite
import com.truekenyan.cocktail.fragments.FragmentHome
import com.truekenyan.cocktail.fragments.FragmentInfo
import com.truekenyan.cocktail.fragments.FragmentSearch
import com.truekenyan.cocktail.utils.Commons
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Callbacks {

    private lateinit var linearLayout: LinearLayout
    private var isHome = true

    private var homeFragment: Fragment? = null
    private var searchFragment: Fragment? = null
    private var favoritesFragment: Fragment? = null
    private var infoFragment: Fragment? = null
    private var doubleBackToExitPressedOnce = false
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        linearLayout = findViewById(R.id.message)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        homeFragment = FragmentHome()
        searchFragment = FragmentSearch()
        favoritesFragment = FragmentFavorite()
        infoFragment = FragmentInfo()

        changeFragment(homeFragment!!, Commons.COCKTAILS)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if (isHome){return@OnNavigationItemSelectedListener true}
                changeFragment(homeFragment!!, Commons.COCKTAILS)
                isHome = true
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                changeFragment(searchFragment!!, Commons.SEARCH)
                isHome = false
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                changeFragment(favoritesFragment!!, Commons.FAVORITES)
                isHome = false
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_info -> {
                changeFragment(infoFragment!!, Commons.INFO)
                isHome = false
                return@OnNavigationItemSelectedListener true
            }
            else -> {
                changeFragment(homeFragment!!, Commons.COCKTAILS)
                isHome = true
                return@OnNavigationItemSelectedListener true
            }
        }
    }

    private fun changeFragment(fragment: Fragment, name: String?){
        val manager = supportFragmentManager
        manager.popBackStack()
        manager.beginTransaction()
                .addToBackStack(fragment.tag)
                .add(R.id.message, fragment)
                .commit()

        toolbar!!.title = name
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

    override fun onTitleFound(name: String?) {
        toolbar!!.title = name
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity()
            }
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}
