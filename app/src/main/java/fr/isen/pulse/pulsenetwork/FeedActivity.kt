package fr.isen.pulse.pulsenetwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import fr.isen.pulse.pulsenetwork.classes.Post
import fr.isen.pulse.pulsenetwork.databinding.ActivityFeedBinding
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import fr.isen.pulse.pulsenetwork.classes.Commentaire


class FeedActivity : AppCompatActivity() {
	private lateinit var binding: ActivityFeedBinding
	private var value: ArrayList<Post> = arrayListOf<Post>()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityFeedBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val actionBar = supportActionBar
		actionBar?.title = "Feed"

		Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app").getReference("pulse/posts").addValueEventListener(object: ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {


				for (postSnapshot in snapshot.children) {
					postSnapshot.getValue<Post>()?.let {
						value.add(it)
						Log.w("GGGGGGGGGGGGGGGG", "Value is: $value")
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

		binding.floatingActionButton.setOnClickListener { view ->
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
	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		val inflater = menuInflater
		inflater.inflate(R.menu.menulight, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
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