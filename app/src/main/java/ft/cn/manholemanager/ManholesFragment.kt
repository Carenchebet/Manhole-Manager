package ft.cn.manholemanager

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ft.cn.manholemanager.models.FibreOpticsManhole

class ManholesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manholes, container, false)

        // Set LayoutManager for RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Fetch data from Firebase and update RecyclerView
        fetchManholesFromFirebase(recyclerView)

        // Set up Add Manhole button click listener
        val addManholeButton = view.findViewById<ImageButton>(R.id.addManholeButton)
        addManholeButton.setOnClickListener {
            showAddManholeDialog()  // Show the form dialog to add a new manhole
        }

        return view
    }
    fun fetchManholesFromFirebase(recyclerView: RecyclerView) {
        val database = FirebaseDatabase.getInstance()
        val manholeRef = database.getReference("manholes")

        // Add a listener to fetch the manholes from Firebase
        manholeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val manholes = mutableListOf<FibreOpticsManhole>()

                // Iterate through the manholes in the database snapshot
                for (manholeSnapshot in snapshot.children) {
                    FibreOpticsManhole(manholeSnapshot.child("location").value.toString(),
                        manholeSnapshot.child("maintainanceStatus").value.toString(),
                        manholeSnapshot.child("locationName").value.toString(),
                        manholeSnapshot.child("region").value.toString(),manholeSnapshot.child("id").value.toString())
                    val manhole = FibreOpticsManhole(manholeSnapshot.child("location").value.toString(),
                        manholeSnapshot.child("maintainanceStatus").value.toString(),
                        manholeSnapshot.child("locationName").value.toString(),
                        manholeSnapshot.child("region").value.toString(),manholeSnapshot.child("id").value.toString())
                    manhole?.let {
                        manholes.add(it)
                    }
                }

                // Update RecyclerView adapter with the fetched manholes
                recyclerView.adapter = FibreOpticsManholeAdapter(manholes)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
                println("Failed to fetch manholes: ${error.message}")
            }
        })
    }
    private fun showAddManholeDialog() {
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.dialog_add_manhole, null)

        // Get input fields
        val locationEditText = view.findViewById<EditText>(R.id.editTextLocation)
        val statusEditText = view.findViewById<EditText>(R.id.editTextStatus)
        val nameEditText = view.findViewById<EditText>(R.id.editTextName)
        val regionEditText = view.findViewById<EditText>(R.id.editTextRegion)

        // Create and show the AlertDialog
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Add New Manhole")
            .setView(view)
            .setPositiveButton("Add") { _, _ ->
                // Handle adding the manhole to Firebase
                val location = locationEditText.text.toString()
                val status = statusEditText.text.toString()
                val name = nameEditText.text.toString()
                val region = regionEditText.text.toString()

                // Create a new FibreOpticsManhole object
                val newManhole = FibreOpticsManhole(location, status, name, region,id="")

                // Add the manhole to Firebase
                addManholeToFirebase(newManhole)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }
    fun addManholeToFirebase(manhole: FibreOpticsManhole) {
        val database = FirebaseDatabase.getInstance()
        val manholeRef = database.reference.child("manholes")

        // Generate a new ID for each manhole
        var manholeId = manholeRef.push().key
         manhole.id=manholeId.toString()
        // Add manhole to Firebase
        manholeId?.let {
            manholeRef.child(it).setValue(manhole)
                .addOnSuccessListener {
                    // Handle success
                    println("Manhole added successfully")
                }
                .addOnFailureListener {
                    // Handle failure
                    println("Failed to add manhole")
                }
        }
    }
}
