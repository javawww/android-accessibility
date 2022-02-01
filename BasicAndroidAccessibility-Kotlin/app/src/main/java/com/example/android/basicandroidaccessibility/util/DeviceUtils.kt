package com.example.android.basicandroidaccessibility.util

import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import java.util.*


object DeviceUtils {

    private fun getAndroidID(context: Context): String? {
        val id: String = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        return id ?: ""
    }

    private fun getDeviceUUid(context: Context): String? {
        val androidId = getAndroidID(context)
        val deviceUuid = UUID(androidId.hashCode().toLong(), androidId.hashCode().toLong() shl 32)
        return deviceUuid.toString()
    }
}