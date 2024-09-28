package ft.cn.manholemanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bottomnav: BottomNavigationView = findViewById(R.id.bottom_nav)
        val toolbar: Toolbar =findViewById(R.id.toolbar)
        supportActionBar?.setIcon(R.drawable.man)  // Custom icon
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)  // Optional: Custom back icon

// Handle back button click
        toolbar.setNavigationOnClickListener {
            onBackPressed()  // Go back to the previous screen
        }

// Default fragment to display (e.g., HomeFragment)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DashboardFragment()).commit()

        bottomnav.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.action_dashboard -> DashboardFragment()
                R.id.action_manholes -> ManholesFragment()
                R.id.action_technicians -> TechniciansFragment()
                else -> null
            }

            // If the fragment is not null, replace the container with the selected fragment
            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, it)
                    .commit()
            }

            // Return true to indicate that the item click has been handled
            true
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle back button click here
                onBackPressed()
                return true
            }
            R.id.action_settings -> {
                // Handle settings action
                return true
            }
            R.id.action_search -> {
                // Handle search action
                showSearchDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun showSearchDialog() {
        // Inflate the custom layout for the dialog
        val dialogView = layoutInflater.inflate(R.layout.dialog_shedule_maintenance, null)

        // Build the AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Search")
            .setView(dialogView)
            .setPositiveButton("Search") { dialogInterface, i ->
                // Handle the search query
                val searchQuery = dialogView.findViewById<EditText>(R.id.editText_search).text.toString()
                performSearch(searchQuery)  // Call your search function
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    // Function to handle search logic
    private fun performSearch(query: String) {
        // Implement your search logic here
        Toast.makeText(this, "Searching for: $query", Toast.LENGTH_SHORT).show()
    }
}