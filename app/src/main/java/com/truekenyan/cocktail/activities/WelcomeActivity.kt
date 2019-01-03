package com.truekenyan.cocktail.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.adapters.IntroAdapter
import com.truekenyan.cocktail.utils.PrefManager
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    private var prefManager: PrefManager? = null
    private val screens = mutableListOf<Int>()
    private var viewPager: ViewPager? = null
    private var introAdapter: IntroAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        init()
    }

    private fun init(){
        prefManager = PrefManager(this@WelcomeActivity)
        screens.add(R.layout.intro_screen_one)
        viewPager = findViewById(R.id.swipe_views)
        introAdapter = IntroAdapter(screens, this@WelcomeActivity)
        viewPager!!.adapter = introAdapter

        skip_button.setOnClickListener {
            launchMain()
        }
    }

    private fun launchMain(){
        prefManager!!.setHasLaunched(true)
        val i = Intent(this@WelcomeActivity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(i)
        finish()
    }
}
