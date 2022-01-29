package com.example.android.basicandroidaccessibility.activity



import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity
import com.example.android.basicandroidaccessibility.R
import com.example.android.basicandroidaccessibility.databinding.ActivityTemplateBinding



class TemplateActivity : AppCompatActivity() {

    private var txtString: TextView? = null
    private var imageSave: ImageView? = null
    private var imageBack: ImageView? = null

    private var _binding: ActivityTemplateBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        txtString = binding.txtString
        imageSave = binding.imageSave
        imageBack = binding.imageBack
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

        // 保存：点击事件
        imageSave?.setOnClickListener {
            Log.d("保存", "保存的点击事件")
            Toast.makeText(this, "保存", Toast.LENGTH_SHORT).show();
        }
        // 返回：点击事件
        imageBack?.setOnClickListener {
            Toast.makeText(this, "返回", Toast.LENGTH_SHORT).show();
        }
    }




}