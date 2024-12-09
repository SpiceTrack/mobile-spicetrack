package com.example.spicetrack.home.ui.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spicetrack.databinding.RowItemSearchBinding
import com.example.spicetrack.home.data.ListSpiceResponseItem
import com.example.spicetrack.home.ui.detail.DetailActivity

class SearchAdapter: ListAdapter<ListSpiceResponseItem, SearchAdapter.SearchSpiceViewHolder>(DIFF_CALLBACK) {

    // ViewHolder
    class SearchSpiceViewHolder(private val binding: RowItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(spice: ListSpiceResponseItem) {
            binding.tvItem.text = spice.title

            Glide.with(binding.ivItem.context)
                .load(spice.imageUrl)
                .into(binding.ivItem)
        }

    }
    // Create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSpiceViewHolder {
        val binding = RowItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchSpiceViewHolder(binding)
    }

    // Bind ViewHolder
    override fun onBindViewHolder(holder: SearchSpiceViewHolder, position: Int) {
        val spice = getItem(position)
        holder.bind(spice)
        holder.itemView.setOnClickListener {
            val intentDetailSpice = Intent(holder.itemView.context, DetailActivity::class.java).apply {
                putExtra("title", spice.title)
                putExtra("subtitle", spice.subtitle)
                putExtra("content", spice.content)
                putExtra("tags", spice.tags?.split(",") as ArrayList<String>)
                putExtra("image_url", spice.imageUrl)
            }
            holder.itemView.context.startActivity(intentDetailSpice)
        }
    }
    // Get Item Count
    override fun getItemCount(): Int {
        return currentList.size
    }

    // DiffUtil
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListSpiceResponseItem>() {
            override fun areItemsTheSame(
                oldItem: ListSpiceResponseItem,
                newItem: ListSpiceResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListSpiceResponseItem,
                newItem: ListSpiceResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}