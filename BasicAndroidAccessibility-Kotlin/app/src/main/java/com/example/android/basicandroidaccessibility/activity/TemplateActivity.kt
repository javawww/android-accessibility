package com.example.android.basicandroidaccessibility.activity



import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.android.basicandroidaccessibility.R

class TemplateActivity : AppCompatActivity() {

    var txtString: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template)

        txtString = findViewById(R.id.txtString)
        // getting the bundle from the intent
        val bundle = intent.extras

        // setting the text in the textview
        txtString?.setText(bundle!!.getString("key1", "No value from MainActivity :("))
    }
}