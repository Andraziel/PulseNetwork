package fr.isen.pulse.pulsenetwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.util.Strings
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
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityFeedBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val actionBar = supportActionBar
		actionBar?.title = "Feed"

		Firebase.database.getReference("posts").addValueEventListener(object: ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				val value = snapshot.child("posts").getValue<Post>()
				Log.d("TAG", "Value is: " + value)
				//findViewById<TextView>(R.id.).text = value.toString()
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
		val value = arrayListOf<Strings>()

		binding.feedList.layoutManager = LinearLayoutManager(this)

		binding.feedList.adapter = FeedAdapter(value) {
			val intent = Intent(this, DetailActivity::class.java)

			//intent.putExtra("post",it)

			startActivity(intent)
		}


	}
}