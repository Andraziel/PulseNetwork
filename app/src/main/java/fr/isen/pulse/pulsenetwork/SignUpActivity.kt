package fr.isen.pulse.pulsenetwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
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
import fr.isen.pulse.pulsenetwork.classes.Post
import fr.isen.pulse.pulsenetwork.classes.UserInfo
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class SignUpActivity : AppCompatActivity() {
	private lateinit var binding: ActivitySignUpBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		binding = ActivitySignUpBinding.inflate(layoutInflater)
		setContentView(binding.root)

		//Display list of schools
		val schoolList = resources.getStringArray(R.array.schoolList)
		val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, schoolList)
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
		binding.schoolSpinner.adapter = adapter


		binding.SuValidateButton.setOnClickListener {
			//Get all the field user has typed
			val InputFirstname = binding.SuFirstname.text.toString()
			val InputLastname = binding.SuLastname.text.toString()
			val InputEmail = binding.SuEmail.text.toString()
			val InputPassword = binding.SuPassword.text.toString()


			val auth = FirebaseAuth.getInstance()

			if(InputFirstname.isEmpty() || InputLastname.isEmpty() || InputEmail.isEmpty() || InputPassword.isEmpty()){
				val toast = Toast.makeText(applicationContext,"Fields can not be empty ! You're crazy...",Toast.LENGTH_SHORT)
				toast.show()
			}else{
				auth.createUserWithEmailAndPassword(InputEmail, InputPassword)
					.addOnCompleteListener(this) { task ->
						if (task.isSuccessful) {
							//get UUID of user created and add in the database the uuid with firstname, lastname
							val userUid = Firebase.auth.currentUser
							Log.w("FB", "UUID is: ${userUid?.uid}")
							val database = Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app")
							val myRef = database.getReference("pulse/users")
							val id = userUid?.uid
							val userInfo = UserInfo(userUid?.uid, InputFirstname, InputLastname, InputEmail)
							id?.let {
								myRef.child(it).setValue(userInfo)
							}

							// Sign in SUCCESSFUL, we now add user's information
							Log.w("FB", "User registration SUCCESSFUL")

							val intent = Intent(this, PostActivity::class.java)
							startActivity(intent)


						} else {
							//Log and print the error in typing
							val error = task.exception
							Log.w("FB", "Error FB: ${error?.message}")
							val toast = Toast.makeText(applicationContext,"Error: ${error?.message}",Toast.LENGTH_SHORT)
							toast.show()
						}

					}
			}

		}
	}
}