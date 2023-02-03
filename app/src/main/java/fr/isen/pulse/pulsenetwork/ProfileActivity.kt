package fr.isen.pulse.pulsenetwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import fr.isen.pulse.pulsenetwork.classes.Post
import fr.isen.pulse.pulsenetwork.classes.UserInfo
import fr.isen.pulse.pulsenetwork.databinding.ActivityProfileBinding
import fr.isen.pulse.pulsenetwork.databinding.ActivityResetMdpBinding
import java.sql.Date
import java.text.SimpleDateFormat

class ProfileActivity : AppCompatActivity() {
	private lateinit var binding: ActivityProfileBinding
	private val userId = FirebaseAuth.getInstance().currentUser?.uid
	private val userMail = FirebaseAuth.getInstance().currentUser?.email
	private val userSignupDate = FirebaseAuth.getInstance().currentUser?.metadata?.creationTimestamp
	private lateinit var userFullName: String
	private lateinit var schoolName: String


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_profile)
		binding = ActivityProfileBinding.inflate(layoutInflater)
		setContentView(binding.root)

		// Getting the user name from the database
		val link = Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app").getReference("pulse/users/$userId")
		link.addListenerForSingleValueEvent(object: ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				val user = snapshot.getValue<UserInfo>()
				userFullName = user?.firstName + " " + user?.lastName
				schoolName = user?.schoolName.toString()

				//Display email
				binding.profileEmail.text = userMail

				//Display name
				binding.profileName.text = userFullName

				//Display name of school
				binding.profileSchool.text = schoolName

				//Display date of sign up
				val date = userSignupDate?.let { Date(it) }
				val format = SimpleDateFormat("dd/MM/yyyy")
				binding.profileSignupDate.text = format.format(date)


			}
			override fun onCancelled(error: DatabaseError) {
				Log.w("PN", "Failed to read value.", error.toException())
			}
		})

		binding.profileChangeEmail.setOnClickListener(){
			//Reset password
			val intent = Intent(this, resetMailActivity::class.java)
			startActivity(intent)
		}

		binding.profileLogOut.setOnClickListener {
			//Signout
			FirebaseAuth.getInstance().signOut()

			Log.w("PN", "Sign out")
			val intent = Intent(this, MainActivity::class.java)
			startActivity(intent)

		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		val inflater = menuInflater
		inflater.inflate(R.menu.menu, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.actionPosts -> {
				// Open FeedActivity
				val intent = Intent(this, FeedActivity::class.java)
				startActivity(intent)
				return true
			}
			R.id.actionProfile -> {
				// Open ProfileActivity
				val intent = Intent(this, ProfileActivity::class.java)
				startActivity(intent)
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}
}


