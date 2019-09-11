package com.truekenyan.cocktail.request

import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    //   alcoholic drinks
    @GET("filter.php?a=Alcoholic")
    fun getAlcoholic(): Call<Any>

    @GET("filter.php?a=Non_Alcoholic")
    fun getNonAlcoholic(): Call<Any>
}