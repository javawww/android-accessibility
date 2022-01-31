package com.example.android.basicandroidaccessibility.`interface`

import com.example.android.basicandroidaccessibility.pojo.Examination
import com.example.android.basicandroidaccessibility.pojo.HttpResult
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface  ExaminationApi {

    /*@GET("/examination")
    fun listAll() : Call<List<Examination>>*/

    @GET("/examination")
    suspend fun listAll2() : Response<List<Examination>>
}