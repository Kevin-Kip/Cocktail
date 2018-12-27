package com.truekenyan.cocktail.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetManager {
    companion object {
        var online: Boolean? = false
        fun isOnline(context: Context?): Boolean{
            val ctx = context!!.applicationContext
            val conManager: ConnectivityManager? = ctx!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? = conManager!!.activeNetworkInfo
            return networkInfo!!.isConnected
        }
    }
}