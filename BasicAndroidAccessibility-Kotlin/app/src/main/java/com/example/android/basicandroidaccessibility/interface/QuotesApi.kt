package com.example.android.basicandroidaccessibility.`interface`

import com.example.android.basicandroidaccessibility.pojo.HttpResult
import com.example.android.basicandroidaccessibility.pojo.Quote
import retrofit2.Response
import retrofit2.http.GET

interface  QuotesApi {

    @GET("/quotes")
    suspend fun getQuotes() : Response<HttpResult<List<Quote>>>
}