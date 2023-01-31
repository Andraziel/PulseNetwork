package fr.isen.pulse.pulsenetwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import fr.isen.pulse.pulsenetwork.classes.Post
import fr.isen.pulse.pulsenetwork.databinding.ActivityFeedBinding

class FeedActivity : AppCompatActivity() {
	private lateinit var binding: ActivityFeedBinding
	private var value: ArrayList<Post> = arrayListOf<Post>()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityFeedBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val actionBar = supportActionBar
		actionBar?.title = "Feed"

/*
		val database = Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app")
		val myRef = database.getReference("pulse/post")
		myRef.push().setValue("Hello, World2!")
		Log.w("-------------", "Value is: " + myRef)
*/

		Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app").getReference("pulse/posts").addValueEventListener(object: ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {


				//value = arrayListOf<Post>()
				for (postSnapshot in snapshot.children) {
					postSnapshot.getValue<Post>()?.let {
						value.add(it)
					}
				}
				val adapter = binding.feedList.adapter as FeedAdapter

				adapter.refresh(value)

				Log.w("TAGGGGG", "Value is: $value")

			}

			override fun onCancelled(error: DatabaseError) {
				Log.w("TAG", "Failed to read value.", error.toException())
			}
		})



		binding.buttonPost.setOnClickListener {// New Post
			val intent = Intent(this, PostActivity::class.java)

			startActivity(intent)
		}

		// Recycler View

		binding.feedList.layoutManager = LinearLayoutManager(this)

		binding.feedList.adapter = FeedAdapter(value) {
			val intent = Intent(this, DetailActivity::class.java)

			intent.putExtra("post",it)

			startActivity(intent)
		}




	}
}