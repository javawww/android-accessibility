package com.example.android.basicandroidaccessibility.util

import android.content.Context
import com.mumu.dialog.MMLoading

object DialogUtil {

    private var mmLoading: MMLoading? = null

    fun showLoading(msg: String?,context: Context) {
        if (mmLoading == null) {
            val builder = MMLoading.Builder(context)
                .setMessage(msg)
                .setCancelable(false)
                .setCancelOutside(false)
            mmLoading = builder.create()
        } else {
            mmLoading!!.dismiss()
            val builder = MMLoading.Builder(context)
                .setMessage(msg)
                .setCancelable(false)
                .setCancelOutside(false)
            mmLoading = builder.create()
        }
        mmLoading!!.show()
    }

    fun hideLoading() {
        if (mmLoading != null && mmLoading!!.isShowing) {
            mmLoading!!.dismiss()
        }
    }
}