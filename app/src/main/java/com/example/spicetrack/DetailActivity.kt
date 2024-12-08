package com.example.spicetrack

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.spicetrack.databinding.ActivityDetailBinding

class DetailActivity : ComponentActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from the intent
        val title = intent.getStringExtra("title")
        val subtitle = intent.getStringExtra("subtitle")
        val content = intent.getStringExtra("content")
        val tags = intent.getStringArrayListExtra("tags") ?: arrayListOf()

        // Display the content in the UI
        binding.titleTextView.text = title
        binding.subtitleTextView.text = subtitle
        binding.contentTextView.text = content
        binding.tagsTextView.text = tags.joinToString(", ")
    }
}
