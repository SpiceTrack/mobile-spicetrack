package com.example.spicetrack.home.ui.search

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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

        // Set appbar
        setSupportActionBar(binding.searchBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up RecyclerView
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSearch.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerViewSearch.addItemDecoration(itemDecoration)


        // Observe loading state
        searchViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        // Observe searched Spice
        searchViewModel.searchedSpice.observe(this) { spice ->
            setSearchSpiceData(spice)
        }

        // Set up SearchView and fetch Spice
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchViewModel.fetchSearchedSpice(it)
                    binding.searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    // Handle Up button press
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // Circular Progress Loading Indicator
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }

    }
    // Give List of Spice to Adapter
    private fun setSearchSpiceData(spice: List<ListSpiceResponseItem?>?) {
        val adapter = SearchAdapter()
        adapter.submitList(spice)
        binding.recyclerViewSearch.adapter = adapter
    }
}