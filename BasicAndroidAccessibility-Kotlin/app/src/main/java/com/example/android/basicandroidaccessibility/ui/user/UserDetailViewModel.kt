package com.example.android.basicandroidaccessibility.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserDetailViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "这是用户详情页面"
    }
    val text: LiveData<String> = _text
}