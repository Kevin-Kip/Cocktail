package com.truekenyan.cocktail.request

import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

object APIService {
    //   alcoholic drinks
    @GET("/filter.php?a=Alcoholic")
    @FormUrlEncoded
    fun getAlcoholic(){

    }
}