package com.truekenyan.cocktail.request

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

interface APIService {
    //   alcoholic drinks
    @GET("/filter.php?a=Alcoholic")
    @FormUrlEncoded
    fun getAlcoholic(): Call<JSONObject>
}