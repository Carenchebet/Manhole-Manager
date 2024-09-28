package ft.cn.manholemanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ft.cn.manholemanager.models.Technician

class TechnicianAdapter(
    private val technicians: List<Technician>
) : RecyclerView.Adapter<TechnicianAdapter.TechnicianViewHolder>() {

    class TechnicianViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val technicianName: TextView = itemView.findViewById(R.id.tvTechnicianName)
        val technicianId: TextView = itemView.findViewById(R.id.tvTechnicianId)
        val expertise: TextView = itemView.findViewById(R.id.tvExpertise)
        val phoneNumber: TextView = itemView.findViewById(R.id.tvPhoneNumber)
        val availabilityStatus: TextView = itemView.findViewById(R.id.tvAvailabilityStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechnicianViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_technician, parent, false)
        return TechnicianViewHolder(view)
    }

    override fun onBindViewHolder(holder: TechnicianViewHolder, position: Int) {
        val technician = technicians[position]
        holder.technicianName.text = technician.name
        holder.technicianId.text = technician.technicianId
        holder.expertise.text = technician.expertise
        holder.phoneNumber.text = technician.phoneNumber
        holder.availabilityStatus.text = technician.availabilityStatus
    }

    override fun getItemCount(): Int {
        return technicians.size
    }
}
