package com.truekenyan.cocktail.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.fragments.FragmentHome
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        linearLayout = findViewById(R.id.message)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val fragment = FragmentHome()
                val manager = supportFragmentManager
                manager.beginTransaction()
                        .add(R.id.message, fragment)
                        .commit()
            }
            R.id.navigation_search -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                return@OnNavigationItemSelectedListener true
            }
            else -> {
                val fragment = FragmentHome()
                val manager = supportFragmentManager
                manager.beginTransaction()
                        .add(R.id.message, fragment)
                        .commit()
            }
        }
        false
    }
}
