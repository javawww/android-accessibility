package com.example.android.basicandroidaccessibility.retrofite

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object  RetrofitHelper {

    private const val baseUrl = "https://quotable.io/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                // 我们需要添加转换器工厂来将 JSON 对象转换为 Java 对象
                .build()
    }

    fun <T> create(clazz: Class<T>):T {
        return  getInstance().create(clazz)
    }
}