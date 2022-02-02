package com.example.android.basicandroidaccessibility.activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.android.basicandroidaccessibility.MainActivity
import com.example.android.basicandroidaccessibility.R
import com.example.android.basicandroidaccessibility.databinding.ActivityAddEditExaminationBinding
import com.example.android.basicandroidaccessibility.databinding.ActivityTemplateBinding
import com.example.android.basicandroidaccessibility.room.entity.Examination
import com.example.android.basicandroidaccessibility.room.viewmodel.ExaminationViewModal
import java.text.SimpleDateFormat
import java.util.*

class AddEditExaminationActivity : AppCompatActivity() {

    private var _binding: ActivityAddEditExaminationBinding? = null
    private val binding get() = _binding!!

    // on below line we are creating
    // variables for our UI components.
    lateinit var noteTitleEdt: EditText
    lateinit var noteEdt: EditText
    lateinit var saveBtn: Button

    // on below line we are creating variable for
    // viewmodal and and integer for our note id.
    lateinit var viewModal: ExaminationViewModal
    var noteID = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddEditExaminationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // on below line we are initialing our view modal.
        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ExaminationViewModal::class.java)

        // on below line we are initializing all our variables.
        noteTitleEdt = binding.idEdtExaminationName
        noteEdt = binding.idEdtExaminationDesc
        saveBtn = binding.idBtn

        // on below line we are getting data passed via an intent.
        val noteType = intent.getStringExtra("noteType")
        if (noteType.equals("Edit")) {
            // on below line we are setting data to edit text.
            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDescription = intent.getStringExtra("noteDescription")
            noteID = intent.getIntExtra("noteId", -1)
            saveBtn.setText("Update Examination")
            noteTitleEdt.setText(noteTitle)
            noteEdt.setText(noteDescription)
        } else {
            saveBtn.setText("Save Examination")
        }

        // 保存或者更新
        saveBtn.setOnClickListener {
            // 获取页面表单数据
            val noteTitle = noteTitleEdt.text.toString()
            val noteDescription = noteEdt.text.toString()
            // on below line we are checking the type
            // and then saving or updating the data.
            if (noteType.equals("Edit")) {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDateAndTime: String = sdf.format(Date())
                    val updatedExamination = Examination(
                        noteTitle, noteDescription, currentDateAndTime,
                        "null","null","null","null","null","null","null",1,"null"
                    )
                    updatedExamination.id = noteID
                    viewModal.updateExamination(updatedExamination)
                    Toast.makeText(this, "Examination Updated..", Toast.LENGTH_LONG).show()
                }
            } else {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDateAndTime: String = sdf.format(Date())
                    // if the string is not empty we are calling a
                    // add note method to add data to our room database.
                    viewModal.addExamination(Examination(
                        noteTitle, noteDescription, currentDateAndTime,
                        "null","null","null","null","null","null","null",1,"null"
                    ))
                    Toast.makeText(this, "$noteTitle Added", Toast.LENGTH_LONG).show()
                }
            }
            // opening the new activity on below line
            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }
}