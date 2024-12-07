package com.example.spicetrack.home.ui.dashboard

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spicetrack.databinding.ActivityDashboardBinding
import com.example.spicetrack.home.ui.dashboard.DashboardAdapter
import com.example.spicetrack.home.ui.dashboard.DashboardViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var adapter: DashboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        adapter = DashboardAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Observe LiveData from ViewModel
        viewModel.articles.observe(this, Observer { articles ->
            adapter.submitList(articles)
        })

        // Fetch Articles from API
        viewModel.fetchArticles()
    }
}
