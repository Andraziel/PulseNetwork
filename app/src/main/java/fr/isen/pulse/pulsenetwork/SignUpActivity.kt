package fr.isen.pulse.pulsenetwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import fr.isen.pulse.pulsenetwork.databinding.ActivitySignUpBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
class SignUpActivity : AppCompatActivity() {
	private lateinit var binding: ActivitySignUpBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		binding = ActivitySignUpBinding.inflate(layoutInflater)
		setContentView(binding.root)

		binding.SuValidateButton.setOnClickListener {
			//Get all the field user has typed
			val InputFirstname = binding.SuFirstname.text.toString()
			val InputLastname = binding.SuLastname.text.toString()
			val InputEmail = binding.SuEmail.text.toString()
			val InputPassword = binding.SuPassword.text.toString()

			val auth = FirebaseAuth.getInstance()

			auth.createUserWithEmailAndPassword(InputEmail, InputPassword)
				.addOnCompleteListener(this) { task ->
					if (task.isSuccessful) {
						// Sign in SUCCESSFUL, we now add user's information
						Log.w("FB", "User registration SUCCESSFUL")

						val user = Firebase.auth.currentUser

						val profileUpdates = userProfileChangeRequest {
							displayName = "$InputFirstname $InputLastname"
						}

						user!!.updateProfile(profileUpdates)
							.addOnCompleteListener { task ->
								if (task.isSuccessful) {
									Log.d("FB", "User profile updated.")
								}
							}

					} else {
						Log.w("FB", "Authentication with FB ERROR")
						val toast = Toast.makeText(
							applicationContext,
							"Error, please try again later!",
							Toast.LENGTH_SHORT
						)
						toast.show()
					}
				}

		}
	}
}