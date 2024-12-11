package com.example.spicetrack.home.ui.search

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spicetrack.R
import com.example.spicetrack.databinding.ActivitySearchBinding
import com.example.spicetrack.home.data.ListSpiceResponseItem

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind with the layout
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        // Set up app bar
//        setSupportActionBar(binding.searchbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up RecyclerView
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSearch.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerViewSearch.addItemDecoration(itemDecoration)

        // Observe loading state
        searchViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        // Observe searched spice data
        searchViewModel.searchedSpice.observe(this) { spice ->
            setSearchSpiceData(spice)
        }

        // Set up SearchView and fetch spice data
        val searchView = binding.searchbar // Pastikan ID sesuai
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchViewModel.fetchSearchedSpice(it)  // Run the search based on query
                    searchView.clearFocus()  // Clear focus after search
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    // Handle Up button press (back navigation)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()  // Go back to previous activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Show or hide loading spinner
    private fun showLoading(isLoading: Boolean) {
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        if (isLoading) {
            progressBar.visibility = View.VISIBLE  // Show ProgressBar when loading
        } else {
            progressBar.visibility = View.GONE  // Hide ProgressBar after loading is complete
        }
    }

    // Set search results to RecyclerView
    private fun setSearchSpiceData(spice: List<ListSpiceResponseItem?>?) {
        val adapter = SearchAdapter()
        adapter.submitList(spice)  // Send data to adapter
        binding.recyclerViewSearch.adapter = adapter  // Set adapter for RecyclerView
    }
}
