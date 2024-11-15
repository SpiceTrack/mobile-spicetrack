package com.example.spicetrack.home


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spicetrack.MainActivity
import com.example.spicetrack.R
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Tunggu beberapa detik sebelum membuka MainActivity
        GlobalScope.launch {
            delay(3000) // 3 detik
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish() // Hentikan SplashActivity agar tidak kembali ke splash screen
        }
    }
}


