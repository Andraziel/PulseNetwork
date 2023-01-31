package fr.isen.pulse.pulsenetwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.pulse.pulsenetwork.classes.Post
import fr.isen.pulse.pulsenetwork.databinding.ActivityMainBinding
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

			val post = Post(description, null, image, titre, 0, 0, "auteur")

			val database = Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app")
			val myRef = database.getReference("pulse/post")
			val id = myRef.push().key
			id?.let {
				myRef.child(it).setValue(post)
			}

			Log.w("-------------", "Value is: " + myRef)
		}


	}
}