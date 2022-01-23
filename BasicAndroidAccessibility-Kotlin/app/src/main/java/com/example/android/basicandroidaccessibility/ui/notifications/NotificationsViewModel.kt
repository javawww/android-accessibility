package com.example.android.basicandroidaccessibility.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "这是通知片段"
    }
    val text: LiveData<String> = _text
}