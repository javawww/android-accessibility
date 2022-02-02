package com.example.android.basicandroidaccessibility.activity
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.android.basicandroidaccessibility.MainActivity
import com.example.android.basicandroidaccessibility.R
import com.example.android.basicandroidaccessibility.databinding.ActivityAddEditExaminationBinding
import com.example.android.basicandroidaccessibility.databinding.ActivityTemplateBinding
import com.example.android.basicandroidaccessibility.filter.InputFilterMinMax
import com.example.android.basicandroidaccessibility.room.entity.Examination
import com.example.android.basicandroidaccessibility.room.viewmodel.ExaminationViewModal
import com.example.android.basicandroidaccessibility.util.DeviceUtils
import kotlinx.android.synthetic.main.activity_add_edit_examination.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

class AddEditExaminationActivity : AppCompatActivity() {

    private var _binding: ActivityAddEditExaminationBinding? = null
    private val binding get() = _binding!!

    // 顶部导航栏
    private lateinit var txtString: TextView
    private lateinit var imageSave: ImageView
    private lateinit var imageBack: ImageView
    // 表单内容
    lateinit var etQuestion: EditText
    lateinit var etAns1: EditText
    lateinit var etAns2: EditText
    lateinit var etAns3: EditText
    lateinit var etAns4: EditText
    lateinit var etCorrect: EditText
    lateinit var etAnalyze: EditText
    // 图片
    var etImage: String? = null
    var BSelectImage: Button? = null
    var IVPreviewImage: ImageView? = null
    val SELECT_PICTURE:Int = 200
    // 校验标识符
    var isAllFieldsChecked: Boolean = false

    // on below line we are creating variable for
    // viewmodal and and integer for our note id.
    lateinit var viewModal: ExaminationViewModal
    var primary = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddEditExaminationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // on below line we are initialing our view modal.
        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ExaminationViewModal::class.java)

        // 绑定导航栏
        txtString = binding.txtString
        imageSave = binding.imageSave
        imageBack = binding.imageBack

        // 表单字段
        etQuestion = binding.question
        etAns1 = binding.ans1
        etAns2 = binding.ans2
        etAns3 = binding.ans3
        etAns4 = binding.ans4
        etCorrect = binding.correct
        etCorrect?.filters = arrayOf<InputFilter>(InputFilterMinMax("1", "4"))
        etAnalyze = binding.analyze

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

        // 编辑数据回显
        val optType = intent.getStringExtra("optType")
        if (optType.equals("Edit")) {
            // on below line we are setting data to edit text.
            val question = intent.getStringExtra("question")
            val ans1 = intent.getStringExtra("ans1")
            val ans2 = intent.getStringExtra("ans2")
            val ans3 = intent.getStringExtra("ans3")
            val ans4 = intent.getStringExtra("ans4")
            val correct = intent.getIntExtra("correct",1)
            val analyze = intent.getStringExtra("analyze")
            primary = intent.getIntExtra("primary", -1)
            //saveBtn.setText("Update Examination")
            etQuestion.setText(question)
            etAns1.setText(ans1)
            etAns2.setText(ans2)
            etAns3.setText(ans3)
            etAns4.setText(ans4)
            etCorrect.setText(correct.toString())
            etAnalyze.setText(analyze)
        } else {
            //saveBtn.setText("Save Examination")
        }

        // 保存：点击事件
        imageSave?.setOnClickListener {
            isAllFieldsChecked = CheckAllFields()
            if (isAllFieldsChecked){
                // 获取页面表单数据
                val uuid = UUID.randomUUID().toString()
                val deviceId = DeviceUtils.getDeviceUUid(this)
                val question = etQuestion.text.toString()
                val ans1 = etAns1.text.toString()
                val ans2 = etAns2.text.toString()
                val ans3 = etAns3.text.toString()
                val ans4 = etAns4.text.toString()
                val correct = etCorrect.text.toString().toInt()
                val analyze = etAnalyze.text.toString()
                // on below line we are checking the type
                // and then saving or updating the data.
                if (optType.equals("Edit")) {
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val lastTime: String = sdf.format(Date())
                    val updatedExamination = Examination(
                        uuid,deviceId!!,lastTime,question,
                        "","",
                        ans1,ans2,ans3,ans4,
                        correct,analyze
                    )
                    updatedExamination.id = primary
                    viewModal.updateExamination(updatedExamination)
                    Toast.makeText(this, "数据 已更新...", Toast.LENGTH_LONG).show()

                } else {
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val lastTime: String = sdf.format(Date())
                    viewModal.addExamination(Examination(
                        uuid,deviceId!!,lastTime,question,
                        "","",
                        ans1,ans2,ans3,ans4,
                        correct,analyze
                    ))
                    Toast.makeText(this, "$question 已新增", Toast.LENGTH_LONG).show()

                }
                // opening the new activity on below line
                //startActivity(Intent(applicationContext, MainActivity::class.java))
                this.finish()
                //exitProcess(0)
            }
        }

        // 返回：点击事件
        imageBack?.setOnClickListener {
            Toast.makeText(this, "返回", Toast.LENGTH_SHORT).show();
            this.finish()
            //exitProcess(0)
        }
    }

    private fun CheckAllFields(): Boolean {
        if (etQuestion?.length() == 0) {
            etQuestion?.error = "测验问题必填"
            return false;
        }
        if (etAns1?.length() == 0) {
            etAns1?.error = "选项1必填"
            return false;
        }
        if (etAns2?.length() == 0) {
            etAns2?.error = "选项2必填"
            return false;
        }
        if (etCorrect?.length() == 0 || etCorrect.text.equals("")) {
            etCorrect?.error = "答案必填"
            return false;
        }
        return true
    }
}