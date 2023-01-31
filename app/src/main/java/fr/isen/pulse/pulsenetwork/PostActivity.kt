package fr.isen.pulse.pulsenetwork

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.pulse.pulsenetwork.classes.Post
import fr.isen.pulse.pulsenetwork.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity() {
	private lateinit var binding: ActivityPostBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_post)
		binding = ActivityPostBinding.inflate(layoutInflater)
		setContentView(binding.root)


		// titrePost, imagePost, descriptionPost, closePost
		binding.closePost.setOnClickListener {
			val intent = Intent(this, FeedActivity::class.java)
			startActivity(intent)
		}

		binding.validePost.setOnClickListener {
			val image = binding.imagePost.text.toString()
			val titre = binding.titrePost.text.toString()
			val description = binding.descriptionPost.text.toString()



			val database = Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app")
			val myRef = database.getReference("pulse/posts")
			//val id = myRef.push().key
			val id = database.getReference("pulse/posts").push().key
			val post = Post(description, id, image, titre, 0, 0, "auteur")
			id?.let {
				myRef.child(it).setValue(post)
			}

			val intent = Intent(this, FeedActivity::class.java)
			startActivity(intent)

			Log.w("-------------", "Value is: " + myRef)
		}


	}
}