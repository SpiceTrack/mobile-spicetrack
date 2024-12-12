package com.example.spicetrack.home.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spicetrack.databinding.ActivityDashboard2Binding
import com.example.spicetrack.home.data.HerpsResponseItem
import com.example.spicetrack.home.ui.search.SearchActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboard2Binding
    private lateinit var dashboardAdapter: DashboardAdapter
    private var herpsList: List<HerpsResponseItem> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboard2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupSearchView()

        val layoutManager = LinearLayoutManager(this)
        binding.rvHerps.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHerps.addItemDecoration(itemDecoration)
        binding.rvHerps.adapter = DashboardAdapter(emptyList())

        dashboardAdapter = DashboardAdapter(emptyList())
        binding.rvHerps.adapter = dashboardAdapter

        val DashboardViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DashboardViewModel::class.java)
        DashboardViewModel.herps.observe(this) { herps ->
            setHerpsData(herps)
        }
    }

    private fun setHerpsData(herps: List<HerpsResponseItem>) {
        Log.d("DashboardActivity", "Herps Data: $herps")
        val adapter = DashboardAdapter(herps)
        binding.rvHerps.adapter = adapter
    }

    private fun setupSearchView() {
        binding.searchBar.setOnClickListener {
            // Start SearchActivity when the SearchView is clicked
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }
}