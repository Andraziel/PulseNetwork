package fr.isen.pulse.pulsenetwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.pulse.pulsenetwork.classes.Post
import fr.isen.pulse.pulsenetwork.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
	private lateinit var binding: ActivityDetailBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDetailBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val post = intent.getSerializableExtra("post") as Post

		val actionBar = supportActionBar
		actionBar?.title = post?.titre





	}
}