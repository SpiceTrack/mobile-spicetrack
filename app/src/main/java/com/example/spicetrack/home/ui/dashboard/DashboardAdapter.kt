package com.example.spicetrack.home.ui.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spicetrack.R
import com.example.spicetrack.databinding.RowItemBinding
import com.example.spicetrack.home.data.HerpsResponseItem
import com.example.spicetrack.home.ui.detail.DetailActivity

class DashboardAdapter(private var listHerps: List<HerpsResponseItem>) :
    RecyclerView.Adapter<DashboardAdapter.HerpsViewHolder>() {

    // Update data by changing the listHerps
    fun updateData(newHerpsList: List<HerpsResponseItem>) {
        this.listHerps = newHerpsList // Now you can reassign the listHerps
        notifyDataSetChanged()  // Notify RecyclerView to update its display
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HerpsViewHolder {
        val binding = RowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HerpsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HerpsViewHolder, position: Int) {
        val herps = listHerps[position]
        with(holder.binding) {
            // Bind data to the views
            tvItem.text = herps.title
            Glide.with(ivItem.context)
                .load(herps.imageUrl)
                .placeholder(R.drawable.ic_placeholder) // Placeholder image
                .error(R.drawable.ic_error) // Error image if loading fails
                .into(ivItem)

            btnReadNow.setOnClickListener {
                val intent = Intent(root.context, DetailActivity::class.java)
                intent.putExtra("HERPS_ID", herps.idHerbs) // Send data to the next Activity
                intent.putExtra("HERPS_TITLE", herps.title)
                intent.putExtra("HERPS_SUBTITLE", herps.subtitle)
                intent.putExtra("HERPS_IMAGE", herps.imageUrl)
                intent.putExtra("HERPS_CONTENT", herps.content)
                intent.putExtra("HERPS_TAGS", herps.tags)
                intent.putExtra("HERPS_ORIGIN", herps.origin)
                root.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = listHerps.size

    class HerpsViewHolder(val binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root)
}
