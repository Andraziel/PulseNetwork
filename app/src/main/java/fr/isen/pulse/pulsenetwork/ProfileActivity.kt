package fr.isen.pulse.pulsenetwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

class ProfileActivity : AppCompatActivity() {
	private lateinit var binding: ActivityProfileBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_profile)
		binding = ActivityProfileBinding.inflate(layoutInflater)
		setContentView(binding.root)

		/*val database = Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app")
		val ref = database.getReference("pulse/users")*/

		Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app").getReference("pulse/posts").addValueEventListener(object:
			ValueEventListener {
			override fun onDataChange(ref: DataSnapshot) {

				val auth = FirebaseAuth.getInstance()
				val user = auth.currentUser
				val userUid = user?.uid

				val mail = user?.email
				if (userUid != null) {
					ref.child(userUid).getValue<UserInfo>().let {
						val prenom = it?.firstName
						val nom = it?.lastName
						binding.prenom.text = prenom.toString()
						binding.nom.text = nom.toString()


					}
				}

				//myRef.child(it).setValue(post)
				//postSnapshot.getValue<Post>()
				/*
				for (postSnapshot in snapshot.children) {
					postSnapshot.getValue<Post>()?.let {
						value.add(it)
					}
				}
				 */


				binding.mail.text = mail.toString()

			}
			override fun onCancelled(error: DatabaseError) {
				Log.w("TAG", "Failed to read value.", error.toException())
			}
		})


		binding.retour.setOnClickListener {
			val intent = Intent(this, FeedActivity::class.java)
			startActivity(intent)
		}
	}
}


