package ft.cn.manholemanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class DashboardFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }
    private fun fetchManholesData() {
        firestore.collection("fibreOpticsManholes")
            .get()
            .addOnSuccessListener { documents ->
                manholeList.clear()
                for (document in documents) {
                    val manhole = document.toObject(FibreOpticsManhole::class.java)
                    manholeList.add(manhole)
                }
                manholeAdapter.notifyDataSetChanged()
                textViewTotalManholes.text = "Total Manholes: ${manholeList.size}"
            }
            .addOnFailureListener { exception ->
                Log.e("DashboardFragment", "Error fetching manholes: ", exception)
            }
    }

    private fun fetchTechniciansData() {
        firestore.collection("technicians")
            .get()
            .addOnSuccessListener { documents ->
                technicianList.clear()
                var availableCount = 0
                for (document in documents) {
                    val technician = document.toObject(Technician::class.java)
                    technicianList.add(technician)
                    if (technician.availabilityStatus.equals("Available", ignoreCase = true)) {
                        availableCount++
                    }
                }
                technicianAdapter.notifyDataSetChanged()
                textViewAvailableTechnicians.text = "Available Technicians: $availableCount"
            }
            .addOnFailureListener { exception ->
                Log.e("DashboardFragment", "Error fetching technicians: ", exception)
            }
    }

}