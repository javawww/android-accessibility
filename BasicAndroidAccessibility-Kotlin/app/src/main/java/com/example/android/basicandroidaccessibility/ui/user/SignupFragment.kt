package com.example.android.basicandroidaccessibility.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.example.android.basicandroidaccessibility.activity.TemplateActivity
import com.example.android.basicandroidaccessibility.databinding.LayoutSignupBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton





// 这里的“：”符号表示SignupFragment是Fragment Class的子类
class SignupFragment : Fragment() {


    // 该属性仅在 onCreateView 和onDestroyView
    private var _binding: LayoutSignupBinding? = null
    private val binding get() = _binding!!

    //确保对所有 FAB 使用 FloatingActionButton
    var mAddFab: FloatingActionButton? =null
    var mAddAlarmFab:FloatingActionButton? = null
    var mAddPersonFab:FloatingActionButton? = null
    // 这些与 FAB 一起被用来使可见和不可见
    var addAlarmActionText: TextView? = null
    var addPersonActionText:TextView? = null
    // 检查子 FAB 按钮是否可见。
    var isAllFabsVisible: Boolean? = null

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
                val intent = Intent(this, TemplateActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)

            }

        }

        // 注册父节点
        mAddFab = binding.addFab
        // 注册子节点
        mAddAlarmFab = binding.addAlarmFab
        mAddPersonFab = binding.addPersonFab
        // 还要注册所有 FAB 的操作名称文本。
        addAlarmActionText = binding.addAlarmActionText
        addPersonActionText = binding.addPersonActionText
        // 现在将所有 FAB 和所有动作名称文本设置为 GONE
        mAddAlarmFab?.visibility = View.GONE
        mAddPersonFab?.visibility = View.GONE
        addAlarmActionText?.visibility = View.GONE
        addPersonActionText?.visibility = View.GONE
        //将布尔变量设置为 false，因为所有操作名称文本和所有子 FAB 都是不可见的
        isAllFabsVisible = false
        // 父组件点击事件
        mAddFab?.setOnClickListener {
            if (!isAllFabsVisible!!){
                mAddAlarmFab?.show()
                mAddPersonFab?.show()
                addAlarmActionText?.visibility = View.VISIBLE
                addPersonActionText?.visibility = View.VISIBLE
                isAllFabsVisible = true
            }else{
                mAddAlarmFab?.hide()
                mAddPersonFab?.hide()
                addAlarmActionText?.visibility = View.GONE
                addPersonActionText?.visibility = View.GONE
                isAllFabsVisible = false
            }
        }
        // Person子组件点击事件
        mAddPersonFab?.setOnClickListener {
            Toast.makeText(context, "Person Added", Toast.LENGTH_SHORT).show();
        }
        // Alarm子组件点击事件
        mAddAlarmFab?.setOnClickListener {
            Toast.makeText(context, "Alarm Added", Toast.LENGTH_SHORT).show();
        }
        return root
    }
    // Here "layout_signup" is a name of layout file
    // created for SignFragment
}
