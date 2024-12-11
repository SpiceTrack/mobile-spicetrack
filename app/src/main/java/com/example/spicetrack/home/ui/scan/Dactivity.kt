package com.example.spicetrack.home.ui.scan

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.spicetrack.R

class Dactivity : AppCompatActivity() {

    private lateinit var classificationTextView: TextView
    private lateinit var urlTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dactivity)

        classificationTextView = findViewById(R.id.classificationTextView)
        urlTextView = findViewById(R.id.urlTextView)

        val classification = intent.getStringExtra("classification")
        val url = intent.getStringExtra("url")

        classificationTextView.text = "Classification: $classification"
        urlTextView.text = "More Info: $url"
    }
}
