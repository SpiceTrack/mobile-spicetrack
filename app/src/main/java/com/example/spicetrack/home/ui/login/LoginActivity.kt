package com.example.spicetrack


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spicetrack.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  kembali ke MainActivity saat ikon panah diklik
        binding.backIcon.setOnClickListener {
            finish()
        }
    }
}
