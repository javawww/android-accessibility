package com.example.android.basicandroidaccessibility.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.android.basicandroidaccessibility.R
import com.example.android.basicandroidaccessibility.databinding.ActivityExaminationBinding
import com.example.android.basicandroidaccessibility.filter.InputFilterMinMax
import kotlin.system.exitProcess


class ExaminationActivity : AppCompatActivity() {

    private var _binding: ActivityExaminationBinding? = null
    private val binding get() = _binding!!

    // 顶部导航栏
    private var txtString: TextView? = null
    private var imageSave: ImageView? = null
    private var imageBack: ImageView? = null
    // 表单内容
    var etCorrect: EditText? = null
    var BSelectImage: Button? = null
    var IVPreviewImage: ImageView? = null
    val SELECT_PICTURE:Int = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityExaminationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 绑定导航栏
        txtString = binding.txtString
        imageSave = binding.imageSave
        imageBack = binding.imageBack

        // 表单字段
        etCorrect = binding.correct
        etCorrect?.filters = arrayOf<InputFilter>(InputFilterMinMax("1", "4"))
        //选择图片和预览图片
        BSelectImage = binding.BSelectImage
        IVPreviewImage = binding.IVPreviewImage
        BSelectImage?.setOnClickListener {
            var intent:Intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"选择图片"),SELECT_PICTURE)
        }

        // 将工具栏的 ID 分配给变量
        val toolbar: Toolbar = findViewById<View>(R.id.toolbarTemplate) as Toolbar
        // 使用工具栏作为 ActionBar
        setSupportActionBar(toolbar)

        // 保存：点击事件
        imageSave?.setOnClickListener {
            Toast.makeText(this, "保存", Toast.LENGTH_SHORT).show();

        }
        // 返回：点击事件
        imageBack?.setOnClickListener {
            Toast.makeText(this, "返回", Toast.LENGTH_SHORT).show();
            this.finish()
            exitProcess(0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            if(requestCode == SELECT_PICTURE){
                Log.d("成功获取图片", "onActivityResult: ")
                val selectedImageUri: Uri? = data?.data
                IVPreviewImage?.setImageURI(selectedImageUri)
            }
        }
    }

}