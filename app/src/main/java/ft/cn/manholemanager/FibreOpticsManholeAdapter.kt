package ft.cn.manholemanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import ft.cn.manholemanager.models.FibreOpticsManhole

class FibreOpticsManholeAdapter(
    private val manholes: List<FibreOpticsManhole>
) : RecyclerView.Adapter<FibreOpticsManholeAdapter.ManholeViewHolder>() {

    class ManholeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val locationName: TextView = itemView.findViewById(R.id.tvLocationName)
        val region: TextView = itemView.findViewById(R.id.tvRegion)
        val location: TextView = itemView.findViewById(R.id.tvLocation)
        val maintenanceStatus: TextView = itemView.findViewById(R.id.tvMaintenanceStatus)
        val btndelete: ImageButton =itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManholeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fibre_optics_manhole, parent, false)
        return ManholeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ManholeViewHolder, position: Int) {
        val manhole = manholes[position]
        holder.locationName.text = manhole.locationName
        holder.region.text = manhole.region
        holder.location.text = manhole.location
        holder.maintenanceStatus.text = manhole.maintenanceStatus
        holder.btndelete.setOnClickListener{
            FirebaseDatabase.getInstance().reference.child("manholes").child(manhole.id).removeValue()
        }
    }

    override fun getItemCount(): Int {
        return manholes.size
    }
}
