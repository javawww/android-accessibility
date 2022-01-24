package com.example.android.basicandroidaccessibility.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.basicandroidaccessibility.databinding.FramentUserDetailBinding

class UserDetailFragment : Fragment() {

    private lateinit var userDetailViewModel: UserDetailViewModel
    private var _binding: FramentUserDetailBinding? = null

    // 此属性仅在 onCreateView 和 onDestroyView 之间有效。
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?{
        userDetailViewModel = ViewModelProvider(this).get(UserDetailViewModel::class.java)
        _binding = FramentUserDetailBinding.inflate(inflater,container,false)
        // 赋值
        val textView: TextView = binding.textDetail
        userDetailViewModel.text.observe(viewLifecycleOwner,{
            textView.text = it
        })
        // bundle 取值
        savedInstanceState?.getString("")
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}