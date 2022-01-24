package com.example.android.basicandroidaccessibility.ui.baidumap

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BaiduMapViewModel : ViewModel()  {

    private val _text = MutableLiveData<String>().apply {
        value = "这是百度地图"
    }
    val text: LiveData<String> = _text
}