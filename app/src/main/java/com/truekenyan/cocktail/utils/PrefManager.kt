package com.truekenyan.cocktail.utils

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences(Commons.FIRST_TIME_LAUNCH, 0)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun setHasLaunched(hasLaunched: Boolean) {
        editor.apply { putBoolean(Commons.HAS_LAUNCHED, hasLaunched) }.apply { apply() }
    }

    fun hasLaunched(): Boolean = sharedPref.getBoolean(Commons.HAS_LAUNCHED, false)

    fun setAlcoholic(isAlcoholic: Boolean) {
        editor.apply { putBoolean(Commons.ALCOHOLIC, isAlcoholic) }.apply { apply() }
    }

    fun isAlcoholic(): Boolean = sharedPref.getBoolean(Commons.ALCOHOLIC, true)

    fun setOrder(order: String?) {
        editor.apply { putString(Commons.ORDER, order) }.apply { apply() }
    }

    fun getOrder(): String? = sharedPref.getString(Commons.ORDER, Commons.RANDOM)

    fun setSearchByName(useName: Boolean) {
        editor.apply { putBoolean(Commons.USE_NAME, useName) }.apply { apply() }
    }

    fun searchByName(): Boolean = sharedPref.getBoolean(Commons.USE_NAME, true)
}