package com.truekenyan.cocktail.request

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    //   alcoholic drinks
    @GET("filter.php?a=Alcoholic")
    fun getAlcoholic(): Call<JSONObject>
}