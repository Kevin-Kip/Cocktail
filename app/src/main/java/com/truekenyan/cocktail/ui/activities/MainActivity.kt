package com.truekenyan.cocktail.ui.activities

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.callbacks.Callbacks
import com.truekenyan.cocktail.ui.fragments.FragmentFavorite
import com.truekenyan.cocktail.ui.fragments.FragmentHome
import com.truekenyan.cocktail.ui.fragments.FragmentInfo
import com.truekenyan.cocktail.ui.fragments.FragmentSearch
import com.truekenyan.cocktail.utils.Commons
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Callbacks {

    private lateinit var linearLayout: LinearLayout
    private var currentFragment: String? = null

    private var homeFragment:Fragment? = null
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
                if (currentFragment == Commons.HOME) {
                    return@OnNavigationItemSelectedListener true
                }
                changeFragment(homeFragment!!, Commons.COCKTAILS)
                currentFragment = Commons.HOME
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                if (currentFragment == Commons.SEARCH) {
                    return@OnNavigationItemSelectedListener true
                }
                changeFragment(searchFragment!!, Commons.SEARCH)
                currentFragment = Commons.SEARCH
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                if (currentFragment == Commons.FAVORITES) {
                    return@OnNavigationItemSelectedListener true
                }
                changeFragment(favoritesFragment!!, Commons.FAVORITES)
                currentFragment = Commons.FAVORITES
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_info -> {
                if (currentFragment == Commons.INFO) {
                    return@OnNavigationItemSelectedListener true
                }
                changeFragment(infoFragment!!, Commons.INFO)
                currentFragment = Commons.INFO
                return@OnNavigationItemSelectedListener true
            }
            else -> {
                changeFragment(homeFragment!!, Commons.COCKTAILS)
                currentFragment = Commons.HOME
                return@OnNavigationItemSelectedListener true
            }
        }
    }

    private fun changeFragment(fragment: Fragment, name: String?) {
        val manager = supportFragmentManager
        manager.popBackStack()
        manager.beginTransaction()
                .addToBackStack(fragment.tag)
                .add(R.id.message, fragment)
                .commit()

        toolbar!!.title = name
    }

    override fun onTitleFound(name: String?) {
        toolbar!!.title = name
    }

    override fun onRemoveClicked(name: String?) {}

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) finishAffinity()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}
