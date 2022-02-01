package com.example.android.basicandroidaccessibility.util

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

object UrlCheckUtil {

    fun reachable(url:String):Boolean{
        val url = URL(url)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 10 * 1000 // 10s
        connection.instanceFollowRedirects = true
        connection.useCaches = false
        var isReachable = false
        try {
            connection.connect()
        }catch (ex:Exception){
            Log.d("网络报错", "reachable: ${ex.message}")
            return isReachable
        }
        val code: Int = connection.responseCode
        if (code in 200..399) { isReachable = true }
        Log.d("网络:$url", "是否可以访问: $isReachable")
        return isReachable!!
    }
}

