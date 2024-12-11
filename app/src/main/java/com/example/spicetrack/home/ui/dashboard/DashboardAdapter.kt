package com.example.spicetrack.home.ui.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spicetrack.databinding.RowItemBinding
import com.example.spicetrack.home.data.HerpsResponseItem
import com.example.spicetrack.home.ui.detail.DetailActivity

class DashboardAdapter(private val listHerps: List<HerpsResponseItem>) :
    RecyclerView.Adapter<DashboardAdapter.HerpsViewHolder>() {

//    private val spices = mutableListOf<ListSpiceResponseItem>()

//    fun updateData(newSpices: List<ListSpiceResponseItem>) {
//        spices.clear()
//        spices.addAll(newSpices)
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HerpsViewHolder {
        val binding = RowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HerpsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HerpsViewHolder, position: Int) {
        val herps = listHerps[position]
        with(holder.binding) {
            // Bind data ke View
            tvItem.text = herps.title
//            tvHerpsSubtitle.text = herps.subtitle
            Glide.with(ivItem.context)
                .load(herps.imageUrl)
                .into(ivItem)

            btnReadNow.setOnClickListener {
                val intent = Intent(root.context, DetailActivity::class.java)
                intent.putExtra("HERPS_ID", herps.idHerbs) // Kirim data ke Activity berikutnya
                intent.putExtra("HERPS_TITLE", herps.title) // Mengirim title
                intent.putExtra("HERPS_SUBTITLE", herps.subtitle) // Mengirim subtitle
                intent.putExtra("HERPS_IMAGE", herps.imageUrl) // Mengirim URL gambar
                intent.putExtra("HERPS_CONTENT", herps.content) // Mengirim URL deskripsi
                intent.putExtra("HERPS_TAGS", herps.tags) // Mengirim URL tags
                intent.putExtra("HERPS_ORIGIN", herps.origin) // Mengirim URL origin
                root.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = listHerps.size

//    inner class SpiceViewHolder(private val binding: RowItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(spice: ListSpiceResponseItem) {
//            binding.tvItem.text = spice.title
//            Glide.with(binding.root.context).load(spice.imageUrl).into(binding.ivItem)
//        }
//    }

    class HerpsViewHolder(val binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root)

}
