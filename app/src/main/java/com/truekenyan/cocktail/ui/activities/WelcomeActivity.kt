package com.truekenyan.cocktail.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.adapters.IntroAdapter
import com.truekenyan.cocktail.utils.PrefManager
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    private var prefManager: PrefManager? = null
    private val screens = mutableListOf<Int>()
    private var introAdapter: IntroAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        init()
    }

    private fun init(){
        prefManager = PrefManager(this@WelcomeActivity)
        screens.add(R.layout.intro_screen_one)
        screens.add(R.layout.intro_screen_two)
        screens.add(R.layout.intro_screen_three)
        introAdapter = IntroAdapter(screens, this@WelcomeActivity)
        swipe_views!!.adapter = introAdapter
        indicator.setViewPager(swipe_views)
        skip_button.setOnClickListener {
            launchMain()
        }
        swipe_views.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(p0: Int) {

            }
        })
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
