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
import com.example.spicetrack.home.ui.scan.ScanActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private val imagesCount = 3 // Total number of slides (3)

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

        // Get Started button
        binding.btnGetStarted.setOnClickListener {
            val registerIntent = Intent(this, DashboardActivity::class.java)
            startActivity(registerIntent)
        }

        startAutoSlider()
    }

    private fun startAutoSlider() {
        val runnable = object : Runnable {
            override fun run() {
                // Update slide in ViewFlipper
                binding.vfImageSlider.displayedChild = currentPage
                updateIndicator(currentPage)
                currentPage = (currentPage + 1) % imagesCount
                handler.postDelayed(this, 2000)
            }
        }
        handler.post(runnable)
    }

    private fun updateIndicator(position: Int) {
        // Reset all dots to inactive
        binding.dot1.setBackgroundResource(R.drawable.circle_inactive)
        binding.dot2.setBackgroundResource(R.drawable.circle_inactive)
        binding.dot3.setBackgroundResource(R.drawable.circle_inactive)

        // Activate the dot corresponding to the displayed slide
        when (position) {
            0 -> binding.dot1.setBackgroundResource(R.drawable.circle_active)
            1 -> binding.dot2.setBackgroundResource(R.drawable.circle_active)
            2 -> binding.dot3.setBackgroundResource(R.drawable.circle_active)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop the handler when the activity is destroyed
        handler.removeCallbacksAndMessages(null)
    }
}
