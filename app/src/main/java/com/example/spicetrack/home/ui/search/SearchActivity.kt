import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spicetrack.databinding.ActivitySearchBinding
import com.example.spicetrack.home.data.HerpsResponseItem
import com.example.spicetrack.home.ui.dashboard.DashboardAdapter
import com.example.spicetrack.home.ui.search.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchAdapter: DashboardAdapter
    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val herpsTitle = intent.getStringExtra("HERPS_TITLE") // Default null jika tidak ada
        val herpsImage = intent.getStringExtra("HERPS_IMAGE")

        supportActionBar?.hide()

        setupRecyclerView()
        observeSearchResults()

        setupSearchView()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSearch.layoutManager = layoutManager
        searchAdapter = DashboardAdapter(emptyList())
        binding.recyclerViewSearch.adapter = searchAdapter
    }

    private fun observeSearchResults() {
        searchViewModel.searchResults.observe(this) { herps ->
            setSearchResults(herps)
        }
    }

    private fun setupSearchView() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchViewModel.searchHerbs(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchViewModel.searchHerbs(it) }
                return true
            }
        })
    }

    private fun setSearchResults(herps: List<HerpsResponseItem>) {
        if (herps.isEmpty()) {
            Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show()
        }
        searchAdapter.updateData(herps)
    }
}
