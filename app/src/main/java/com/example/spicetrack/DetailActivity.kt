package com.example.spicetrack

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.spicetrack.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : ComponentActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan data dari intent
        val title = intent.getStringExtra("title")
        val subtitle = intent.getStringExtra("subtitle")
        val content = intent.getStringExtra("content")
        val tags = intent.getStringArrayListExtra("tags") ?: arrayListOf()
        val imageUrl = intent.getStringExtra("image_url") // Untuk gambar (jika ada)

        // Menetapkan data ke view
        binding.title.text = title ?: "Tidak Ada Judul"
        binding.scientificName.text = subtitle ?: "Tidak Ada Nama Ilmiah"
        binding.description.text = content ?: "Tidak Ada Deskripsi"

        // Menetapkan tag jika ada, jika tidak, kosongkan tampilan tag
        if (tags.isNotEmpty()) {
            binding.tag1.text = tags.getOrNull(0) ?: ""
            binding.tag2.text = tags.getOrNull(1) ?: ""
        } else {
            binding.tag1.text = ""
            binding.tag2.text = ""
        }

        // Jika URL gambar diberikan, muat gambar ke ImageView
        imageUrl?.let {
            Glide.with(this)
                .load(it)
                .into(binding.imageMain)
        }

        // Menetapkan nilai tetap untuk asal dan bahasa (jika tidak diberikan melalui intent)
        binding.origin.text = intent.getStringExtra("origin") ?: "Asal Tidak Diketahui"
        binding.language.text = intent.getStringExtra("language") ?: "Bahasa Tidak Diketahui"
    }
}
