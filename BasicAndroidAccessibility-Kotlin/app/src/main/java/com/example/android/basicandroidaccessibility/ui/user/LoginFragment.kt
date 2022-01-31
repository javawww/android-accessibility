package com.example.android.basicandroidaccessibility.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.android.basicandroidaccessibility.`interface`.ExaminationApi
import com.example.android.basicandroidaccessibility.databinding.LayoutLoginBinding
import com.example.android.basicandroidaccessibility.pojo.Examination
import com.example.android.basicandroidaccessibility.pojo.HttpResult
import com.example.android.basicandroidaccessibility.retrofite.RetrofitHelper
import com.example.android.basicandroidaccessibility.util.DialogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

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
        /*val examinationApi = RetrofitHelper.create(ExaminationApi::class.java)
        val examinationCall: Call<List<Examination>> = examinationApi.listAll()
        examinationCall.enqueue(object : Callback<List<Examination>>{
            override fun onResponse(call: Call<List<Examination>>, response: Response<List<Examination>>
            ) {
                if(response.isSuccessful){
                    Log.d("接口成功", "onResponse:${response.body()} ")
                }
            }
            override fun onFailure(call: Call<List<Examination>>, t: Throwable) {
                Log.d("接口报错", "onFailure: ${t.message} ")
            }
        })*/
       context?.let { DialogUtil.showLoading("加载中...", it) }
        val examinationApi = RetrofitHelper.create(ExaminationApi::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            val result = examinationApi.listAll2()
            Log.d("结果: ", "${result.body()}")
            val examinations: List<Examination>? = result.body()
            withContext(Dispatchers.Main){
                textDesc.text = examinations?.get(0)?.analyze ?: "欢迎访问"
                DialogUtil.hideLoading()
            }
        }

        return root
    }
    // 这里“layout_login”是为 LoginFragment 创建的布局文件的名称
}
