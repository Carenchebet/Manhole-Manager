package ft.cn.manholemanager

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import ft.cn.manholemanager.AddNewTechnician
import ft.cn.manholemanager.TechnicianAdapter
import ft.cn.manholemanager.models.Technician

class TechniciansFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var technicianList: MutableList<Technician>
    private lateinit var technicianAdapter: TechnicianAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_technicians, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        technicianList = mutableListOf()
        technicianAdapter = TechnicianAdapter(technicianList)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = technicianAdapter

        database = FirebaseDatabase.getInstance().getReference("Technicians")

        // Attach listener to read data
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                technicianList.clear()
                for (technicianSnapshot in snapshot.children) {
                    val technician =Technician(technicianSnapshot.child("technicianId").value.toString(),technicianSnapshot.child("name").value.toString(),technicianSnapshot.child("expertise").value.toString(),technicianSnapshot.child("phoneNumber").value.toString(),technicianSnapshot.child("availabilityStatus").value.toString())
                    technician?.let { technicianList.add(it) }
                }
                technicianAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        view.findViewById<FloatingActionButton>(R.id.addNewButton).setOnClickListener {
            startActivity(Intent(requireContext(), AddNewTechnician::class.java))
        }
        return view
    }
}
