package ft.cn.manholemanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var database:FirebaseDatabase
    private lateinit var auth:FirebaseAuth
    private lateinit var username:EditText
    private lateinit var password:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database=FirebaseDatabase.getInstance()
        val ref=database.reference
        auth=FirebaseAuth.getInstance()
        username=findViewById(R.id.email_input)
        password= findViewById(R.id.password_input)

        val btnlogin:Button=findViewById(R.id.login_button)
        btnlogin.setOnClickListener {
            auth.signInWithEmailAndPassword(username.text.toString(),password.text.toString()).addOnCompleteListener {
                if(it.isSuccessful)
                {
                    ref.child("users").child(it.result.user!!.uid.toString()).addValueEventListener(object:ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val role=snapshot.child("role").value.toString()
                            if(role=="admin")
                            {
                                startActivity(Intent(this@MainActivity,Home::class.java))
                            }
                            else{
                                Toast.makeText(this@MainActivity,"you logedin as a technician",Toast.LENGTH_LONG).show()

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })

                }
                else{
                    Toast.makeText(this,"Wrong username or password",Toast.LENGTH_LONG).show()
                }
            }

        }
    }
}