package com.truekenyan.cocktail.utils

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.io.IOException

class CacheManager(val context: Context) {
    fun writeToCache(fileName: String,jsonResponse: String?){
        clearCache()
        try {
            val file = File(context.applicationInfo.dataDir + Commons.CACHE_DIR)
            if (!file.exists()) file.mkdir()
            val cacheFile = FileWriter(file.absolutePath + "/$fileName")
            cacheFile.apply {
                write(jsonResponse)
                flush()
                close()
            }
        } catch (e: IOException){
            Log.d("WRITING CACHE:", e.localizedMessage)
        }
    }

    fun clearCache(){

    }
}