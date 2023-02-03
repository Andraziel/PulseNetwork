package fr.isen.pulse.pulsenetwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.pulse.pulsenetwork.databinding.ActivityResetMdpBinding

class resetMdp : AppCompatActivity() {
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    public lateinit var binding: ActivityResetMdpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_mdp)
        binding = ActivityResetMdpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val link = Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app").getReference("pulse/users/$userId")
        val user = FirebaseAuth.getInstance().currentUser


        val mail = binding.ancien.text
        val nouvmail = binding.nouveau.text
        val conf = binding.nnouveau.text
        binding.change.setOnClickListener() {

                user?.updateEmail(nouvmail.toString())
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("TAG", "User email address updated.")
                        }
                    }
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

        }

    }
}