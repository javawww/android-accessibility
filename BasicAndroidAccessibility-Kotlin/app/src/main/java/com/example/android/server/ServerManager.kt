package com.example.android.server

import android.content.Context
import android.util.Log
import com.yanzhenjie.andserver.AndServer
import com.yanzhenjie.andserver.Server
import com.yanzhenjie.andserver.Server.ServerListener
import java.util.concurrent.TimeUnit


class ServerManager constructor(context: Context?) {

    private var mServer: Server? = null

    init {
        mServer = context?.let {
            AndServer.webServer(it)
                .port(8080)
                .timeout(10, TimeUnit.SECONDS)
                .listener(object : ServerListener {
                    override fun onStarted() {
                        Log.d("onStarted", ": The server started successfully.")
                        Log.d("startServer", "网址: ${mServer?.inetAddress?.hostAddress}:${mServer?.port}" )
                    }
                    override fun onStopped() {
                        Log.d("onStopped", ": The server has stopped.")
                    }
                    override fun onException(e: Exception) {
                        Log.d("onException", ": An exception occurred while the server was starting.")
                    }
                })
                .build()
        }
    }

    fun startServer() {
        if (mServer!!.isRunning) {
            Log.d("startServer", ": The server is already up.")
        } else {
            mServer!!.startup()
        }
    }

    fun stopServer() {
        if (mServer!!.isRunning) {
            mServer!!.shutdown()
        } else {
            Log.w("AndServer", "The server has not started yet.")
        }
    }
}