package fr.isen.pulse.pulsenetwork

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import fr.isen.pulse.pulsenetwork.classes.Post
import fr.isen.pulse.pulsenetwork.classes.UserInfo
import fr.isen.pulse.pulsenetwork.databinding.ActivityPostBinding
import java.io.IOException
import java.util.*

class PostActivity : AppCompatActivity() {
	private lateinit var binding: ActivityPostBinding
	private val uid = FirebaseAuth.getInstance().currentUser?.uid
	private lateinit var fullName: String
	private val IMAGE_PICK_CODE = 1000
	private var selectedImageUri: Uri? = null
	private var url: String? = null
	private var imageUid: String? = null
	private lateinit var firebaseStore: FirebaseStorage
	private lateinit var storageReference: StorageReference
	private lateinit var displayed_image_post: ImageView


	// Fonction pour ouvrir la galerie pour sélectionner une image
	fun openGallery() {
		val intent = Intent(Intent.ACTION_PICK)
		intent.type = "image/*"
		startActivityForResult(intent, IMAGE_PICK_CODE)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_post)
		binding = ActivityPostBinding.inflate(layoutInflater)
		setContentView(binding.root)

		// Initialize Firebase Storage variables
		firebaseStore = FirebaseStorage.getInstance()
		storageReference = FirebaseStorage.getInstance().reference


		// titrePost, imagePost, descriptionPost, closePost
		binding.closePost.setOnClickListener {
			val intent = Intent(this, FeedActivity::class.java)
			startActivity(intent)
		}

		val button: Button = findViewById(R.id.selectImagePost)

		// Lorsque l'utilisateur appuie sur le bouton
		button.setOnClickListener {
			openGallery()
		}
		displayed_image_post = findViewById(R.id.displayedImagePost)


		// Getting the user name from the database
		val link = Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app").getReference("pulse/users/$uid")
		link.addListenerForSingleValueEvent(object: ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				val user = snapshot.getValue<UserInfo>()
				fullName = user?.firstName + " " + user?.lastName
			}
			override fun onCancelled(error: DatabaseError) {
				Log.w("TAG", "Failed to read value.", error.toException())
			}
		})

		binding.validePost.setOnClickListener {

			uploadImage()

			val intent = Intent(this, FeedActivity::class.java)
			startActivity(intent)

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
			val ref = storageReference?.child("myImages/$imageUid")
			val uploadTask = ref?.putFile(selectedImageUri!!)?.addOnSuccessListener {
				ref.downloadUrl.addOnSuccessListener { uri ->
					url = uri.toString()
					val titre = binding.titrePost.text.toString()
					val description = binding.descriptionPost.text.toString()



					val database = Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app")
					val myRef = database.getReference("pulse/posts")
					val id = myRef.push().key
					val post = Post(description, id, url, titre, 0, 0, fullName)
					id?.let {
						myRef.child(it).setValue(post)
					}
				}
			}

		}else{
			Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
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

