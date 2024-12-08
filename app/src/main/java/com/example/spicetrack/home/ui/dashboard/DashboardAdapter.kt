package com.example.spicetrack.home.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spicetrack.databinding.RowItemBinding
import com.example.spicetrack.home.data.Spice

class DashboardAdapter : RecyclerView.Adapter<DashboardAdapter.SpiceViewHolder>() {

    private val spices = mutableListOf<Spice>()

    fun updateData(newSpices: List<Spice>) {
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
        fun bind(spice: Spice) {
            binding.tvItem.text = spice.name
            Glide.with(binding.root.context).load(spice.imageUrl).into(binding.ivItem)
        }
    }
}
