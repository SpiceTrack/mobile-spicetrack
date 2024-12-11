package com.example.spicetrack.home.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spicetrack.R
import com.example.spicetrack.databinding.ActivityDashboardBinding
import com.example.spicetrack.home.data.ListSpiceResponseItem
import com.example.spicetrack.home.ui.scan.ScanActivity
import com.example.spicetrack.home.ui.search.SearchActivity


class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var adapter: DashboardAdapter
    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menangani padding untuk UI yang mengikuti tepi layar (Edge-to-Edge)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Menyambungkan RecyclerView dengan Adapter
        adapter = DashboardAdapter()
        binding.recyclerView.adapter = adapter

        // Mengambil data dari ViewModel
        viewModel = DashboardViewModel()
        viewModel.spices.observe(this, { response ->
            // Convert the response to Spices
            val spices = mapResponseToSpices(response)
            adapter.updateData(spices)  // Pass converted data to the adapter
        })

        // Memanggil fungsi untuk mengambil data
        viewModel.fetchSpices()
    }

    // Helper function to map ListSpiceResponseItem to Spices
    private fun mapResponseToSpices(response: List<ListSpiceResponseItem>): List<ListSpiceResponseItem> {
        return response.map { item ->
            ListSpiceResponseItem(
                title = item.title,
                imageUrl = item.imageUrl
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search_bar -> {
                // Menangani aksi pencarian
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.scan_icon -> {
                // Menangani aksi pemindaian
                val intent = Intent(this, ScanActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}