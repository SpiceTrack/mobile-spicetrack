package com.example.spicetrack.home.ui.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.example.spicetrack.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import com.example.spicetrack.R
import com.google.android.material.chip.Chip
import org.json.JSONArray
import org.json.JSONException

class DetailActivity : ComponentActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        // Mendapatkan data dari intent
        val herpsId = intent.getIntExtra("HERPS_ID", -1) // Default -1 jika tidak ada
        val herpsTitle = intent.getStringExtra("HERPS_TITLE") // Default null jika tidak ada
        val herpsSubtitle = intent.getStringExtra("HERPS_SUBTITLE")
        val herpsImage = intent.getStringExtra("HERPS_IMAGE")
        val herpsContent = intent.getStringExtra("HERPS_CONTENT")
        val herpsTags = intent.getStringExtra("HERPS_TAGS")
        val herpsOrigin = intent.getStringExtra("HERPS_ORIGIN")
//        val title = intent.getStringExtra("title")
//        val subtitle = intent.getStringExtra("subtitle")
//        val content = intent.getStringExtra("content")
//        val tags = intent.getStringArrayListExtra("tags") ?: arrayListOf()
//        val imageUrl = intent.getStringExtra("image_url") // Untuk gambar (jika ada)
//
//        // Menetapkan data ke view
        binding.title.text = herpsTitle
        binding.subtitle.text = herpsSubtitle
        binding.description.text = herpsContent
        binding.origin.text = herpsOrigin
        if (!herpsImage.isNullOrEmpty()) {
            Glide.with(this)
                .load(herpsImage)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error)
                .into(binding.ivItem)
        } else {
            binding.ivItem.setImageResource(R.drawable.ic_error) // Default image
        }


        // Menampilkan tags (dikonversi dari JSON string ke List)
        val tagsList = parseTags(herpsTags) // Fungsi untuk parsing JSON string ke List<String>
        populateTags(tagsList)
    }

    // Fungsi untuk parsing tags dari JSON string
    private fun parseTags(tags: String?): List<String> {
        return try {
            val jsonArray = JSONArray(tags)
            List(jsonArray.length()) { i -> jsonArray.getString(i) }
        } catch (e: JSONException) {
            emptyList() // Return list kosong jika parsing gagal
        }
    }

    // Fungsi untuk menambahkan tags ke ChipGroup
    private fun populateTags(tagsList: List<String>) {
        val chipGroup = binding.chipGroupTags
        chipGroup.removeAllViews() // Hapus Chip sebelumnya


        tagsList.forEach { tag ->
            val chip = Chip(this)
            chip.text = tag
            chip.isClickable = false
            chip.isCheckable = false
            chip.setChipBackgroundColorResource(R.color.brown) // Warna latar belakang
            chip.setTextColor(ContextCompat.getColor(this, R.color.white)) // Warna teks
            chipGroup.addView(chip)
        }
    }
}
