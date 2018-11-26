package com.truekenyan.cocktail.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.utils.Commons

class CockTailActivity : AppCompatActivity() {

    private var drinkId = 0
    private lateinit var i: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cock_tail)

        i = intent
        if (i.hasExtra(Commons.DRINK_ID)){
            drinkId = i.getIntExtra(Commons.DRINK_ID, 0)
        }
    }
}
