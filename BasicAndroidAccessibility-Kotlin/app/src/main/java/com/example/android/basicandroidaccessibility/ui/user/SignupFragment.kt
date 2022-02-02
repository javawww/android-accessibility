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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.basicandroidaccessibility.activity.AddEditExaminationActivity
import com.example.android.basicandroidaccessibility.databinding.LayoutSignupBinding
import com.example.android.basicandroidaccessibility.room.adapter.ExaminationRVAdapter
import com.example.android.basicandroidaccessibility.room.adapter.ExaminationClickDeleteInterface
import com.example.android.basicandroidaccessibility.room.adapter.ExaminationClickInterface
import com.example.android.basicandroidaccessibility.room.entity.Examination
import com.example.android.basicandroidaccessibility.room.viewmodel.ExaminationViewModal
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson


// 这里的“：”符号表示SignupFragment是Fragment Class的子类
class SignupFragment : Fragment(), ExaminationClickInterface, ExaminationClickDeleteInterface {

    // 该属性仅在 onCreateView 和onDestroyView
    private var _binding: LayoutSignupBinding? = null
    private val binding get() = _binding!!

    // on below line we are creating a variable
    // for our recycler view, exit text, button and viewmodel.
    lateinit var viewModal: ExaminationViewModal
    lateinit var examinationsRV: RecyclerView
    
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
        textDetail.visibility = View.GONE
        // 绑定点击事件
        textDetail.setOnClickListener{
            Log.d("setOnClickListener", "触发点击事件: ")
        }

        /**
         * 数据列表渲染
         */
        examinationsRV = binding.examinationsRV
        examinationsRV.layoutManager = LinearLayoutManager(context)

        // 初始化我们的适配器类。
        val examinationRVAdapter = context?.let { ExaminationRVAdapter(it, this, this) }
        // 将适配器设置为我们的页面列表视图
        examinationsRV.adapter = examinationRVAdapter

        // on below line we are
        // initializing our view modal.
        viewModal = ViewModelProvider(this).get(ExaminationViewModal::class.java)

        // on below line we are calling all examinations method
        // from our view modal class to observer the changes on list.
        viewModal.allExaminations.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                // on below line we are updating our list.
                examinationRVAdapter?.updateList(it)
            }
        })

        /**
         * 操作按钮事件注册
         */
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
            requireActivity().run {
                // creating the bundle instance
                val bundle = Bundle()
                // passing the data into the bundle
                bundle?.putString("key1", "Passing Bundle From SignupFragment to 2nd Activity")
                val intent = Intent(this, AddEditExaminationActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
        // Alarm子组件点击事件
        mAddAlarmFab?.setOnClickListener {
            Toast.makeText(context, "Alarm Added", Toast.LENGTH_SHORT).show();
        }
        return root
    }

    override fun onDeleteIconClick(examination: Examination) {
        viewModal.deleteExamination(examination)
        // displaying a toast message
        Toast.makeText(context, "${examination.question} Deleted", Toast.LENGTH_LONG).show()
    }

    override fun onExaminationClick(examination: Examination) {
        Log.d("onExaminationClick", ": ${Gson().toJson(examination)}")
        // opening a new intent and passing a data to it.
        val intent = Intent(context, AddEditExaminationActivity::class.java)
        intent.putExtra("optType", "Edit")
        intent.putExtra("primary", examination.id)
        intent.putExtra("question", examination.question)
        intent.putExtra("ans1", examination.ans1)
        intent.putExtra("ans2",examination.ans2)
        intent.putExtra("ans3",examination.ans3)
        intent.putExtra("ans4",examination.ans4)
        intent.putExtra("correct",examination.correct)
        intent.putExtra("analyze",examination.analyze)
        startActivity(intent)
    }
    // Here "layout_signup" is a name of layout file
    // created for SignFragment
}
