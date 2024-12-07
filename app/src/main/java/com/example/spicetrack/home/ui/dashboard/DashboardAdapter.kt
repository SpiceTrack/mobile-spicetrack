import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spicetrack.R
import com.example.spicetrack.home.data.Spice

class DashboardAdapter : RecyclerView.Adapter<DashboardAdapter.SpiceViewHolder>() {

    private val spices = mutableListOf<Spice>()

    fun submitList(newSpices: List<Spice>) {
        spices.clear()
        spices.addAll(newSpices)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return SpiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpiceViewHolder, position: Int) {
        holder.bind(spices[position])
    }

    override fun getItemCount(): Int = spices.size

    class SpiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val spiceImage: ImageView = itemView.findViewById(R.id.spice_image)
        private val spiceName: TextView = itemView.findViewById(R.id.spice_name)
        private val spiceDescription: TextView = itemView.findViewById(R.id.spice_description)

        fun bind(spice: Spice) {
            spiceName.text = spice.name
            spiceDescription.text = spice.description
            Glide.with(itemView.context)
                .load(spice.imageUrl)
                .into(spiceImage)
        }
    }
}