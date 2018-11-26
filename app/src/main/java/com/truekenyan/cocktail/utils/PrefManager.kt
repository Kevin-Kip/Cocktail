package com.truekenyan.cocktail.utils

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences(Commons.FIRST_TIME_LAUNCH, 0)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun setHasLaunched(hasLaunched:Boolean){
        editor.apply {
            putBoolean(Commons.HAS_LAUNCHED, hasLaunched)
            apply()
        }
    }

    fun hasLaunched(): Boolean {
        return sharedPref.getBoolean(Commons.HAS_LAUNCHED, true)
    }
}