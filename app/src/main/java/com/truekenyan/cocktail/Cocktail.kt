package com.truekenyan.cocktail

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.truekenyan.cocktail.ui.activities.MainActivity
import com.truekenyan.cocktail.ui.activities.WelcomeActivity
import com.truekenyan.cocktail.utils.PrefManager

class Cocktail : Application() {
    override fun onCreate() {
        super.onCreate()
        val i = when(PrefManager(applicationContext).hasLaunched()) {
            true -> Intent(this@Cocktail, MainActivity::class.java)
            else -> Intent(this@Cocktail, WelcomeActivity::class.java)
        }
        i.flags = FLAG_ACTIVITY_CLEAR_TASK.or(FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
    }
}