package com.truekenyan.cocktail.request

object APIClient {
    private const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"
    fun getAPIService(): APIService = RetrofitClient.getClient(BASE_URL).create(APIService::class.java)
}