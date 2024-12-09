package com.example.spicetrack.home.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spicetrack.databinding.RowItemBinding

class DashboardAdapter : RecyclerView.Adapter<DashboardAdapter.SpiceViewHolder>() {

    private val spices = mutableListOf<Spices>()

    fun updateData(newSpices: List<Spices>) {
        spices.clear()
        spices.addAll(newSpices)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpiceViewHolder {
        val binding = RowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpiceViewHolder, position: Int) {
        holder.bind(spices[position])
    }

    override fun getItemCount(): Int = spices.size

    inner class SpiceViewHolder(private val binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(spice: Spices) {
            binding.tvItem.text = spices.title
            Glide.with(binding.root.context).load(spice.imageUrl).into(binding.ivItem)
        }
    }
}
