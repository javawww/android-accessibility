package com.example.android.basicandroidaccessibility.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "这是主页片段"
    }
    val text: LiveData<String> = _text
}