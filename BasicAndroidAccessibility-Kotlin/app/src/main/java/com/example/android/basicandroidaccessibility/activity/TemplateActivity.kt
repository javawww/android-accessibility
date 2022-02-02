package com.example.android.basicandroidaccessibility.activity



import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.android.basicandroidaccessibility.R
import com.example.android.basicandroidaccessibility.databinding.ActivityTemplateBinding
import kotlin.system.exitProcess


class TemplateActivity : AppCompatActivity() {


    private var _binding: ActivityTemplateBinding? = null
    private val binding get() = _binding!!

    // 顶部导航栏
    private var txtString: TextView? = null
    private var imageSave: ImageView? = null
    private var imageBack: ImageView? = null

    // 表单字段
    var etFirstName: EditText? = null
    var etNickname:EditText? = null
    var etEmail:EditText? = null
    var etPassword:EditText? = null

    // 校验标识符
    var isAllFieldsChecked: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 绑定导航栏
        txtString = binding.txtString
        imageSave = binding.imageSave
        imageBack = binding.imageBack
        // 表单内容
        etFirstName = binding.firstName
        etNickname = binding.nickname
        etEmail = binding.email
        etPassword = binding.password

        // getting the bundle from the intent
        val bundle = intent.extras
        bundle!!.getString("key1")?.let { Log.d("接受参数", it) }

        // setting the text in the textview
        txtString?.setText(bundle!!.getString("key1", "No value from MainActivity :("))

        // 将工具栏的 ID 分配给变量
        val toolbar: Toolbar = findViewById<View>(R.id.toolbarTemplate) as Toolbar

        // 使用工具栏作为 ActionBar
        setSupportActionBar(toolbar)

        // 在工具栏中显示应用程序图标
//        supportActionBar?.setDisplayShowHomeEnabled(true)
        //supportActionBar?.setLogo(R.drawable.logo_youtube)
//        supportActionBar?.setDisplayUseLogoEnabled(true)

        // 注册所有 EditText 字段。


        // 保存：点击事件
        imageSave?.setOnClickListener {
            Toast.makeText(this, "保存", Toast.LENGTH_SHORT).show();
            isAllFieldsChecked = CheckAllFields()
            if (isAllFieldsChecked){
                Log.d("校验成功", "保存数据库中...")
            }
        }
        // 返回：点击事件
        imageBack?.setOnClickListener {
            Toast.makeText(this, "返回", Toast.LENGTH_SHORT).show();
            this.finish()
            exitProcess(0)
        }
    }

    private fun CheckAllFields(): Boolean {
        if (etFirstName?.length() == 0) {
            etFirstName?.error = "This field is required"
            return false;
        }
        if (etNickname?.length() == 0) {
            etNickname?.error = "This field is required"
            return false;
        }
        if (etEmail?.length() == 0) {
            etEmail?.error = "Email is required"
            return false;
        }
        if (etPassword?.length() == 0) {
            etPassword?.error = "Password is required"
            return false;
        } else if (etPassword?.length()!! < 8) {
            etPassword?.error = "Password must be minimum 8 characters"
            return false
        }
        return true
    }


}