package com.example.android.basicandroidaccessibility.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.android.basicandroidaccessibility.R
import com.example.android.basicandroidaccessibility.activity.SecondActivity
import com.example.android.basicandroidaccessibility.databinding.LayoutSignupBinding


// 这里的“：”符号表示SignupFragment是Fragment Class的子类
class SignupFragment : Fragment() {


    // 该属性仅在 onCreateView 和onDestroyView
    private var _binding: LayoutSignupBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutSignupBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 文本内容
        val textDetail: TextView = binding.displayMessage
        // 绑定点击事件
        textDetail.setOnClickListener{
            Log.d("setOnClickListener", "触发点击事件: ")
            requireActivity().run {
                // creating the bundle instance
                val bundle = Bundle()
                // passing the data into the bundle
                bundle?.putString("key1", "Passing Bundle From SignupFragment to 2nd Activity")
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)

            }

        }

        return root
    }
    // Here "layout_signup" is a name of layout file
    // created for SignFragment
}
