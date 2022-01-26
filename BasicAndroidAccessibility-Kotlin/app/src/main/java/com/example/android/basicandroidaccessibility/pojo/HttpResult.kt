package com.example.android.basicandroidaccessibility.pojo

data class HttpResult<T>(
        val count: Int,
        val lastItemIndex: Int,
        val page: Int,
        val results: T,
        val totalCount: Int,
        val totalPages: Int
)
