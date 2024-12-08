package com.example.spicetrack

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spicetrack.databinding.ActivityDashboardBinding
import com.example.spicetrack.home.ui.dashboard.DashboardAdapter
import com.example.spicetrack.home.ui.dashboard.DashboardViewModel

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var adapter: DashboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        adapter = DashboardAdapter()
        binding.rowItem.layoutManager = LinearLayoutManager(this)
        binding.rowList.adapter = adapter

        // Observe ViewModel
        viewModel.spices.observe(this) { spices ->
            adapter.updateData(spices)
        }

        // Fetch data
        viewModel.fetchSpices()
    }
}