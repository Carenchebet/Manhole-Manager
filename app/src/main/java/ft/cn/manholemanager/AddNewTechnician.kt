package ft.cn.manholemanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import ft.cn.manholemanager.models.Technician
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddNewTechnician : AppCompatActivity() {

    private lateinit var technicianIdEditText: TextInputEditText
    private lateinit var nameEditText: TextInputEditText
    private lateinit var expertiseEditText: TextInputEditText
    private lateinit var phoneNumberEditText: TextInputEditText
    private lateinit var availabilityStatusSpinner: Spinner
    private lateinit var submitButton: Button

    // Firebase Realtime Database reference
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_technician)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("Technicians")

        // Initialize Views
        technicianIdEditText = findViewById(R.id.technicianIdEditText)
        nameEditText = findViewById(R.id.nameEditText)
        expertiseEditText = findViewById(R.id.expertiseEditText)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        availabilityStatusSpinner = findViewById(R.id.availabilityStatusSpinner)
        submitButton = findViewById(R.id.submitButton)

        // Set up Spinner Adapter for Availability Status
        ArrayAdapter.createFromResource(
            this,
            R.array.availability_status_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            availabilityStatusSpinner.adapter = adapter
        }

        // Handle Submit Button Click
        submitButton.setOnClickListener {
            addTechnician()
        }
    }

    private fun addTechnician() {
        val technicianId = technicianIdEditText.text.toString().trim()
        val name = nameEditText.text.toString().trim()
        val expertise = expertiseEditText.text.toString().trim()
        val phoneNumber = phoneNumberEditText.text.toString().trim()
        val availabilityStatus = availabilityStatusSpinner.selectedItem.toString()

        if (technicianId.isEmpty()) {
            technicianIdEditText.error = "Technician ID is required"
            technicianIdEditText.requestFocus()
            return
        }

        if (name.isEmpty()) {
            nameEditText.error = "Name is required"
            nameEditText.requestFocus()
            return
        }

        if (expertise.isEmpty()) {
            expertiseEditText.error = "Expertise is required"
            expertiseEditText.requestFocus()
            return
        }

        if (phoneNumber.isEmpty()) {
            phoneNumberEditText.error = "Phone Number is required"
            phoneNumberEditText.requestFocus()
            return
        }

        // Validate Phone Number Format
        if (!android.util.Patterns.PHONE.matcher(phoneNumber).matches()) {
            phoneNumberEditText.error = "Enter a valid phone number"
            phoneNumberEditText.requestFocus()
            return
        }

        // Create Technician Object
        val technician = Technician(
            technicianId = technicianId,
            name = name,
            expertise = expertise,
            phoneNumber = phoneNumber,
            availabilityStatus = availabilityStatus
        )

        // Save Technician to Firebase Realtime Database
        database.child(technicianId).setValue(technician)
            .addOnSuccessListener {
                Toast.makeText(this@AddNewTechnician, "Technician Added Successfully", Toast.LENGTH_LONG).show()
                startActivity(Intent(this,Home::class.java))
                clearForm()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@AddNewTechnician, "Failed to add technician: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun clearForm() {
        technicianIdEditText.text?.clear()
        nameEditText.text?.clear()
        expertiseEditText.text?.clear()
        phoneNumberEditText.text?.clear()
        availabilityStatusSpinner.setSelection(0)
    }
}
