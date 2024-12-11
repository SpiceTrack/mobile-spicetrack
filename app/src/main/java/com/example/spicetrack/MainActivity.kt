package com.example.spicetrack

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spicetrack.databinding.ActivityMainBinding
import com.example.spicetrack.home.ui.dashboard.DashboardActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private val imagesCount = 3 // Total jumlah slide (3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Edge-to-Edge UI
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//         Tombol Get Started
        binding.btnGetStarted.setOnClickListener {
//            val registerIntent = Intent(this, DashboardActivity::class.java)
//            startActivity(registerIntent)
            startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
        }


//        startAutoSlider()
    }

//    private fun startAutoSlider() {
//        val runnable = object : Runnable {
//            override fun run() {
//                // Perbarui slide di ViewFlipper
//                binding.vfImageSlider.displayedChild = currentPage
//                updateIndicator(currentPage)
//                currentPage = (currentPage + 1) % imagesCount // Looping slide
//                handler.postDelayed(this, 2000) // Ganti slide setiap 2 detik
//            }
//        }
//        handler.post(runnable)
//    }
//
//    private fun updateIndicator(position: Int) {
//        // Reset semua dots ke tidak aktif
//        binding.dot1.setBackgroundResource(R.drawable.circle_inactive)
//        binding.dot2.setBackgroundResource(R.drawable.circle_inactive)
//        binding.dot3.setBackgroundResource(R.drawable.circle_inactive)
//
//        // Aktifkan dot sesuai slide yang tampil
//        when (position) {
//            0 -> binding.dot1.setBackgroundResource(R.drawable.circle_active)
//            1 -> binding.dot2.setBackgroundResource(R.drawable.circle_active)
//            2 -> binding.dot3.setBackgroundResource(R.drawable.circle_active)
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        handler.removeCallbacksAndMessages(null) // Hentikan handler saat activity dihancurkan
//    }
}
