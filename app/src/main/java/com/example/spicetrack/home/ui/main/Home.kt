package com.example.spicetrack.home.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spicetrack.databinding.ActivityHomeBinding
import com.example.spicetrack.home.ui.scan.Scan

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate layout menggunakan ViewBinding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Terapkan Window Insets menggunakan ViewBinding
        ViewCompat.setOnApplyWindowInsetsListener(binding.home) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnScan.setOnClickListener {
            val registerIntent = Intent(this , Scan::class.java)
            startActivity(registerIntent)
        }
    }
}
