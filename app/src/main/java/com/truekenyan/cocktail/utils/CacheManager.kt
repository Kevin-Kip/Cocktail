package com.truekenyan.cocktail.utils

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException

class CacheManager(val context: Context) {
    fun writeToCache(fileName: String,jsonResponse: String?){
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

    fun readJsonFile(fileName: String): String? {
        try {
            val checkFile = File(context.applicationInfo.dataDir + "${Commons.CACHE_DIR}$fileName")

            if (!checkFile.exists()) return null

            val file = FileInputStream(checkFile)
            val size = file.available()
            val buffer = ByteArray(size)
            file.apply {
                read(buffer)
                close()
            }
            return String(buffer)
        } catch (e: IOException) {
            Log.d("READING CACHE:", e.localizedMessage)
        }
        return null
    }

    fun clearCache() {
        val file = File(context.applicationInfo.dataDir + Commons.CACHE_DIR)
        if(file.exists()) {
            val files = file.listFiles()
            for (f: File in files) f.delete()
        }
    }

    fun clearCache(fileName: String) {
        val file = File(context.applicationInfo.dataDir + "${Commons.CACHE_DIR}$fileName")
        if(file.exists()) file.delete()
    }
}