package ft.cn.manholemanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Initialize the map fragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Fetch locations from Firebase Realtime Database
        fetchLocations()
    }

    private fun fetchLocations() {
        // Reference to the "locations" node in Firebase
        val database = FirebaseDatabase.getInstance()
        val locationsRef = database.getReference("locations")

        locationsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mMap.clear() // Clear existing markers

                for (locationSnapshot in snapshot.children) {
                    val latitude = locationSnapshot.child("latitude").getValue(Double::class.java)
                    val longitude = locationSnapshot.child("longitude").getValue(Double::class.java)

                    if (latitude != null && longitude != null) {
                        val location = LatLng(latitude, longitude)
                        mMap.addMarker(MarkerOptions().position(location).title("Location Marker"))

                        // Move the camera to the first location
                        if (snapshot.children.iterator().hasNext()) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
