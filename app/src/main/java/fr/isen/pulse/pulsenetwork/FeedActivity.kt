package fr.isen.pulse.pulsenetwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.util.Strings
import fr.isen.pulse.pulsenetwork.databinding.ActivityFeedBinding

class FeedActivity : AppCompatActivity() {
	private lateinit var binding: ActivityFeedBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityFeedBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val actionBar = supportActionBar
		actionBar?.title = "Feed"

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