package com.example.android.basicandroidaccessibility.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.android.basicandroidaccessibility.R
import com.example.android.basicandroidaccessibility.`interface`.QuotesApi
import com.example.android.basicandroidaccessibility.databinding.FragmentHomeBinding
import com.example.android.basicandroidaccessibility.databinding.LayoutLoginBinding
import com.example.android.basicandroidaccessibility.pojo.HttpResult
import com.example.android.basicandroidaccessibility.pojo.Quote
import com.example.android.basicandroidaccessibility.retrofite.RetrofitHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// 这里的“：”符号表示LoginFragment是Fragment Class的子类
class LoginFragment : Fragment() {

    // 该属性仅在 onCreateView 和onDestroyView
    private var _binding: LayoutLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // 文本内容
        val textDesc: TextView = binding.textDesc
        //启动一个新的协程
        val quotesApi = RetrofitHelper.create(QuotesApi::class.java)
        GlobalScope.launch {
            val result = quotesApi.getQuotes()
            var quoteList: HttpResult<List<Quote>>? = result.body()
            Log.d("ayush: ", quoteList.toString())
            Log.d("ayush: ", quoteList?.results.toString())
            textDesc.text = "哈哈哈哈哈"
        }

        return root
    }
    // 这里“layout_login”是为 LoginFragment 创建的布局文件的名称
}
