package com.truekenyan.cocktail

import android.app.Application
import android.content.Intent
import com.truekenyan.cocktail.activities.MainActivity
import com.truekenyan.cocktail.activities.WelcomeActivity
import com.truekenyan.cocktail.utils.PrefManager

class Cocktail : Application() {
    override fun onCreate() {
        super.onCreate()
        if (PrefManager(applicationContext).hasLaunched()){
            startActivity(Intent(this@Cocktail, MainActivity::class.java))
        } else {
            startActivity(Intent(this@Cocktail, WelcomeActivity::class.java))
        }
    }
}