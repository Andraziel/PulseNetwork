package fr.isen.pulse.pulsenetwork

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import fr.isen.pulse.pulsenetwork.classes.Post
import fr.isen.pulse.pulsenetwork.classes.UserInfo
import fr.isen.pulse.pulsenetwork.databinding.ActivityProfileBinding
import fr.isen.pulse.pulsenetwork.databinding.ActivityChangeMailBinding
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {
	private lateinit var binding: ActivityProfileBinding
	private val userId = FirebaseAuth.getInstance().currentUser?.uid
	private val userMail = FirebaseAuth.getInstance().currentUser?.email
	private val userSignupDate = FirebaseAuth.getInstance().currentUser?.metadata?.creationTimestamp
	private lateinit var userFullName: String
	private lateinit var schoolName: String
	private val IMAGE_PICK_CODE = 1000
	private var selectedImageUri: Uri? = null
	private var url: String? = null
	private var imageUid: String? = null
	private lateinit var firebaseStore: FirebaseStorage
	private lateinit var storageReference: StorageReference
	private lateinit var displayed_image_post: ImageView

	fun openGallery() {
		val intent = Intent(Intent.ACTION_PICK)
		intent.type = "image/*"
		startActivityForResult(intent, IMAGE_PICK_CODE)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_profile)
		binding = ActivityProfileBinding.inflate(layoutInflater)
		setContentView(binding.root)

		// Initialize Firebase Storage variables
		firebaseStore = FirebaseStorage.getInstance()
		storageReference = FirebaseStorage.getInstance().reference




		// Getting the user name from the database
		val link = Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app").getReference("pulse/users/$userId")
		link.addValueEventListener(object: ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				val user = snapshot.getValue<UserInfo>()
				userFullName = user?.firstName + " " + user?.lastName
				schoolName = user?.schoolName.toString()

				//Display Profile Picture
				val imageProfil = binding.imageView10
				val newimage = user?.image
				if (newimage != "") { if (newimage != null) {
					Picasso.get().load(newimage).into(imageProfil) }}

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
			val intent = Intent(this, ChangeMailActivity::class.java)
			startActivity(intent)
		}

		binding.profileLogOut.setOnClickListener {
			//Signout
			FirebaseAuth.getInstance().signOut()
			Log.w("PN", "Sign out")
			val intent = Intent(this, MainActivity::class.java)
			startActivity(intent)
		}

		binding.profileChangeAvatar.setOnClickListener {
			openGallery()
			uploadImage()
		}
		displayed_image_post = findViewById(R.id.imageView10)
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

	// Gestion du résultat lorsque l'utilisateur sélectionne une image
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
			selectedImageUri = data?.data
			displayed_image_post.setImageURI(selectedImageUri)
		}
	}

	private fun uploadImage(){
		if(selectedImageUri != null){
			imageUid = UUID.randomUUID().toString()
			val ref = storageReference?.child("$userId/$imageUid")
			val uploadTask = ref?.putFile(selectedImageUri!!)?.addOnSuccessListener {
				ref.downloadUrl.addOnSuccessListener { uri ->
					url = uri.toString()
					val database = Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app")
					database.getReference("pulse/users/$userId/image").setValue(url)
				}
			}

		}else{
			Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
		}
	}
}


