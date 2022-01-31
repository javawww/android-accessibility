package com.example.android.basicandroidaccessibility.filter

import android.text.InputFilter
import android.text.Spanned
import android.util.Log

class InputFilterMinMax:InputFilter {

    private var min = 0
    private var max = 0

    constructor(min: Int, max: Int) {
        this.min = min
        this.max = max
    }
    constructor(min: String, max: String) {
        this.min = min.toInt()
        this.max = max.toInt()
    }
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val input = (dest.toString() + source.toString()).toInt()
            if (isInRange(min, max, input)){
                return null
            }
        } catch (nfe: NumberFormatException) {
            Log.d("报错", "filter:${nfe.message} ")
        }
        return ""
    }
    private fun isInRange(a: Int, b: Int, c: Int): Boolean {
        return if (b > a) c in a..b else c in b..a
    }

}