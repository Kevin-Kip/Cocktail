package com.truekenyan.cocktail

import android.app.Application
import android.content.Intent
import com.truekenyan.cocktail.activities.MainActivity

class Cocktail : Application() {
    override fun onCreate() {
        super.onCreate()
        val i = Intent(this@Cocktail, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(i)
//        if (PrefManager(applicationContext).hasLaunched()){
//            val i = Intent(this@Cocktail, MainActivity::class.java).apply {
//                flags = FLAG_ACTIVITY_NEW_TASK
//            }
//            startActivity(i)
//        } else {
//            val i = Intent(this@Cocktail, WelcomeActivity::class.java).apply {
//                flags = FLAG_ACTIVITY_NEW_TASK
//            }
//            startActivity(i)
//        }
    }
}