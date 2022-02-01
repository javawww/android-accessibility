package com.example.android.basicandroidaccessibility.util

import android.util.Log
import java.net.URL
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

object UrlCheckUtil {
    fun reachable(url:String):Boolean{
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            val acceptedIssuers: Array<Any?>?
                get() = null
            @Throws(CertificateException::class)
            override fun checkClientTrusted(arg0: Array<X509Certificate?>?, arg1: String?) {
                // Not implemented
            }
            @Throws(CertificateException::class)
            override fun checkServerTrusted(arg0: Array<X509Certificate?>?, arg1: String?) {
                // Not implemented
            }
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })

        val sc: SSLContext = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
        // Create all-trusting host name verifier
        val allHostsValid: HostnameVerifier = HostnameVerifier { _, _ -> true }
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)

        val url = URL(url)
        val connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
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

