package com.example.spicetrack.home.ui.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spicetrack.databinding.ActivityDashboard2Binding
import com.example.spicetrack.home.data.HerpsResponseItem

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboard2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboard2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val layoutManager = LinearLayoutManager(this)
        binding.rvHerps.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHerps.addItemDecoration(itemDecoration)


        val DashboardViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DashboardViewModel::class.java)
        DashboardViewModel.herps.observe(this) { herps ->
            setHerpsData(herps)
        }
    }

    private fun setHerpsData(herps: List<HerpsResponseItem>) {
        val adapter = DashboardAdapter(herps)
        binding.rvHerps.adapter = adapter
    }
}