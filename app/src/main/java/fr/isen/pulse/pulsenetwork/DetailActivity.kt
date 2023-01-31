package fr.isen.pulse.pulsenetwork

import android.R
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import fr.isen.pulse.pulsenetwork.classes.Post
import fr.isen.pulse.pulsenetwork.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {
	private lateinit var binding: ActivityDetailBinding

	var mIvToggle: ImageView? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDetailBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val post = intent.getSerializableExtra("post") as Post

		val actionBar = supportActionBar
		actionBar?.title = post?.titre

		binding.auteurPost.text = post.auteur
		binding.descriptionPost.text = post.description
		binding.titrePost.text = post.titre

/*
		binding.ivToggle?.setOnClickListener {
			//fun onClick(view: View?) {
				mIvToggle!!.isActivated = !mIvToggle!!.isActivated
			//}
		}*/


		/*
		val image = post.image
		if (image != "") { if (image != null) { Picasso.get().load(image).into(binding.imagePost) }}
		*/



	}
}