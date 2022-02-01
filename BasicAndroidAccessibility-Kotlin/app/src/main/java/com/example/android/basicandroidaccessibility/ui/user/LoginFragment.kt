package com.example.android.basicandroidaccessibility.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.basicandroidaccessibility.`interface`.ExaminationApi
import com.example.android.basicandroidaccessibility.activity.AddEditNoteActivity
import com.example.android.basicandroidaccessibility.activity.ExaminationActivity
import com.example.android.basicandroidaccessibility.activity.TemplateActivity
import com.example.android.basicandroidaccessibility.databinding.LayoutLoginBinding
import com.example.android.basicandroidaccessibility.pojo.Examination
import com.example.android.basicandroidaccessibility.pojo.HttpResult
import com.example.android.basicandroidaccessibility.retrofite.RetrofitHelper
import com.example.android.basicandroidaccessibility.room.adapter.NoteClickDeleteInterface
import com.example.android.basicandroidaccessibility.room.adapter.NoteClickInterface
import com.example.android.basicandroidaccessibility.room.adapter.NoteRVAdapter
import com.example.android.basicandroidaccessibility.room.entity.Note
import com.example.android.basicandroidaccessibility.room.viewmodel.NoteViewModal
import com.example.android.basicandroidaccessibility.util.DialogUtil
import com.example.android.basicandroidaccessibility.util.UrlCheckUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.SSLException
import kotlin.math.log

// 这里的“：”符号表示LoginFragment是Fragment Class的子类
class LoginFragment : Fragment() , NoteClickInterface, NoteClickDeleteInterface {

    // 该属性仅在 onCreateView 和onDestroyView
    private var _binding: LayoutLoginBinding? = null
    private val binding get() = _binding!!

    // on below line we are creating a variable
    // for our recycler view, exit text, button and viewmodel.
    lateinit var viewModal: NoteViewModal
    lateinit var notesRV: RecyclerView

    //确保对所有 FAB 使用 FloatingActionButton
    var mAddFab: FloatingActionButton? =null

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

        GlobalScope.launch(Dispatchers.IO) {
            val isReachable = UrlCheckUtil.reachable(RetrofitHelper.baseUrl)
            if(isReachable){
                withContext(Dispatchers.Main){
                    context?.let { DialogUtil.showLoading("加载中...", it) }
                    val examinationApi = RetrofitHelper.create(ExaminationApi::class.java)
                    val result = examinationApi.listAll2()
                    Log.d("结果: ", "${result.body()}")
                    val examinations: List<Examination>? = result.body()
                    textDesc.text = examinations?.get(0)?.analyze ?: "欢迎访问"
                    DialogUtil.hideLoading()
                }
            }
        }


        notesRV = binding.notesRV
        // on below line we are setting layout
        // manager to our recycler view.
        notesRV.layoutManager = LinearLayoutManager(context)

        // on below line we are initializing our adapter class.
        val noteRVAdapter = context?.let { NoteRVAdapter(it, this, this) }

        // on below line we are setting
        // adapter to our recycler view.
        notesRV.adapter = noteRVAdapter

        // on below line we are
        // initializing our view modal.
        viewModal = ViewModelProvider(this).get(NoteViewModal::class.java)

        // on below line we are calling all notes method
        // from our view modal class to observer the changes on list.
        viewModal.allNotes.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                // on below line we are updating our list.
                noteRVAdapter?.updateList(it)
            }
        })

        // 注册父节点
        mAddFab = binding.addExamination
        mAddFab?.setOnClickListener{
            Toast.makeText(context, "新增测验记录", Toast.LENGTH_SHORT).show();
            val intent = Intent(context, AddEditNoteActivity::class.java)
            startActivity(intent)
        }
        return root
    }

    override fun onDeleteIconClick(note: Note) {
        // in on note click method we are calling delete
        // method from our view modal to delete our not.
        viewModal.deleteNote(note)
        // displaying a toast message
        Toast.makeText(context, "${note.noteTitle} Deleted", Toast.LENGTH_LONG).show()
    }

    override fun onNoteClick(note: Note) {
        // opening a new intent and passing a data to it.
        val intent = Intent(context, AddEditNoteActivity::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", note.noteTitle)
        intent.putExtra("noteDescription", note.noteDescription)
        intent.putExtra("noteId", note.id)
        startActivity(intent)
        //this.activity?.finish()
    }
    // 这里“layout_login”是为 LoginFragment 创建的布局文件的名称
}
