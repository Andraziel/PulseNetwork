package fr.isen.pulse.pulsenetwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import fr.isen.pulse.pulsenetwork.databinding.ActivityChangeMailBinding


class ChangeMailActivity : AppCompatActivity() {
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    lateinit var binding: ActivityChangeMailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_mail)
        binding = ActivityChangeMailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = FirebaseAuth.getInstance().currentUser

        val newEmailInput = binding.newEmailInput.text

        binding.changeSaveButton.setOnClickListener() {
            if(newEmailInput.isEmpty()){
                //Security on field
                val toast = Toast.makeText(applicationContext,"Field can not be empty ! You're crazy...",Toast.LENGTH_SHORT)
                toast.show()
            }
            user?.updateEmail(newEmailInput.toString())
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "User email address updated.")
                        val toast = Toast.makeText(applicationContext,"Email changed! You re the best!",
                            Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }

            //Sign out and start activity sign in
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

    }
}