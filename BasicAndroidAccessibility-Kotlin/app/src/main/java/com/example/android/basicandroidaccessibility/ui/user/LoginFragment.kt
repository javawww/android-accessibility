package com.example.android.basicandroidaccessibility.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.basicandroidaccessibility.R

// 这里的“：”符号表示LoginFragment是Fragment Class的子类
class LoginFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
                R.layout.layout_login, container, false
        )
    }
    // 这里“layout_login”是为 LoginFragment 创建的布局文件的名称
}
