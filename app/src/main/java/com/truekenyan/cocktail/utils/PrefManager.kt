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

    fun setAlcoholic(isAlcoholic: Boolean){
        editor.apply {
            putBoolean(Commons.ALCOHOLIC, isAlcoholic)
            apply()
        }
    }

    fun isAlcoholic(): Boolean{
        return sharedPref.getBoolean(Commons.ALCOHOLIC, true)
    }

    fun setOrder(order: String?){
        editor.apply {
            putString(Commons.ORDER, order)
            apply()
        }
    }

    fun getOrder(): String? {
        return sharedPref.getString(Commons.ORDER, Commons.RANDOM)
    }

    fun setSearchByName(useName: Boolean){
        editor.apply {
            putBoolean(Commons.USE_NAME, useName)
            apply()
        }
    }

    fun searchByName(): Boolean {
        return sharedPref.getBoolean(Commons.USE_NAME, true)
    }
}