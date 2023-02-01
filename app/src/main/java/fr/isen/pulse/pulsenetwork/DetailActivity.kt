package fr.isen.pulse.pulsenetwork

import android.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.CompoundButton
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
		binding.nbLikes.text = post.like.toString()
		binding.nbDislikes.text = post.dislike.toString()

		// Animation for toggle button
		var scaleAnimation = ScaleAnimation(
			0.7f,
			1.0f,
			0.7f,
			1.0f,
			Animation.RELATIVE_TO_SELF,
			0.7f,
			Animation.RELATIVE_TO_SELF,
			0.7f
		)
		scaleAnimation?.setDuration(500)
		var bounceInterpolator = BounceInterpolator()
		scaleAnimation?.setInterpolator(bounceInterpolator)

		// Toggle Button Code
		val toggle_like = binding.likePost
		toggle_like.setOnCheckedChangeListener(object:View.OnClickListener, CompoundButton.OnCheckedChangeListener {
			override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
				p0?.startAnimation(scaleAnimation) //To change body of created functions use File | Settings | File Templates.
				if (p1) {
					// The toggle is enabled
					binding.nbLikes.text = (binding.nbLikes.text.toString().toInt()?.plus(1)).toString()

				} else {
					// The toggle is disabled
					binding.nbLikes.text = (binding.nbLikes.text.toString().toInt()?.minus(1)).toString()
				}
			}

			override fun onClick(p0: View?) {
				TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
			}
		})


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