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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.database.ktx.snapshots
import com.google.firebase.ktx.Firebase
import fr.isen.pulse.pulsenetwork.classes.Post
import fr.isen.pulse.pulsenetwork.databinding.ActivityDetailBinding
import kotlinx.coroutines.flow.toList


class DetailActivity : AppCompatActivity() {
	private lateinit var binding: ActivityDetailBinding
	private lateinit var post: Post

	private val uid = FirebaseAuth.getInstance().currentUser?.uid

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDetailBinding.inflate(layoutInflater)
		setContentView(binding.root)

		post = intent.getSerializableExtra("post") as Post

		// modification of the action bar
		val actionBar = supportActionBar
		actionBar?.title = post?.titre

		// Modification of the elements on the front
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

		// Create ref to database
		val database = Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app").getReference("pulse/posts/${post.id}")

		// Toggle Button Like
		val toggle_like = binding.likePost
		// Toggle Button Dislike
		val toggle_dislike = binding.dislikePost


		// Listener to know the state of the toggles buttons
		database.addListenerForSingleValueEvent(object: ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {

				val listChildsLike = snapshot.child("likes").getValue<ArrayList<String>>()?.contains(uid)
				if (listChildsLike != null) {
					toggle_like.setChecked(listChildsLike)
				}

				val listChildsDislike = snapshot.child("dislikes").getValue<ArrayList<String>>()?.contains(uid)
				if (listChildsDislike != null) {
					toggle_dislike.setChecked(listChildsDislike)
				}

				val link = Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app").getReference("pulse/posts/${post.id}")

				toggle_like.setOnCheckedChangeListener(object:View.OnClickListener, CompoundButton.OnCheckedChangeListener {
					override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
						p0?.startAnimation(scaleAnimation)
						if (p1) {
							// The toggle is enabled
							binding.nbLikes.text = (binding.nbLikes.text.toString().toInt()?.plus(1)).toString()
							onLikeClicked(link)

						} else {
							// The toggle is disabled
							binding.nbLikes.text = (binding.nbLikes.text.toString().toInt()?.minus(1)).toString()
							onLikeClicked(link)
						}
					}

					override fun onClick(p0: View?) {
						TODO("not implemented")
					}
				})

				toggle_dislike.setOnCheckedChangeListener(object:View.OnClickListener, CompoundButton.OnCheckedChangeListener {
					override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
						p0?.startAnimation(scaleAnimation)
						if (p1) {
							// The toggle is enabled
							binding.nbDislikes.text = (binding.nbDislikes.text.toString().toInt()?.plus(1)).toString()
							onDislikeClicked(link)

						} else {
							// The toggle is disabled
							binding.nbDislikes.text = (binding.nbDislikes.text.toString().toInt()?.minus(1)).toString()
							onDislikeClicked(link)

						}
					}

					override fun onClick(p0: View?) {
						TODO("not implemented")
					}
				})



			}

			override fun onCancelled(error: DatabaseError) {
				Log.w("TAG", "Failed to read value.", error.toException())
			}
		})






		/*
		val image = post.image
		if (image != "") { if (image != null) { Picasso.get().load(image).into(binding.imagePost) }}
		*/



	}



	private fun onLikeClicked(postRef: DatabaseReference) {
		postRef.runTransaction(object : Transaction.Handler {
			override fun doTransaction(mutableData: MutableData): Transaction.Result {
				val p = mutableData.getValue(Post::class.java)
					?: return Transaction.success(mutableData)

				if (p.likes?.contains(uid) == true) {
					// Unlike the post and remove self from likes
					p.like = p.like?.minus(1)
					p.likes?.remove(uid)
				} else {
					// Like the post and add self to likes
					p.like = p.like?.plus(1)
					if (uid != null) {
						p.likes?.add(uid)
					}
				}

				// Set value and report transaction success
				mutableData.value = p
				return Transaction.success(mutableData)
			}

			override fun onComplete(
				databaseError: DatabaseError?,
				committed: Boolean,
				currentData: DataSnapshot?
			) {
				// Transaction completed
			}
		})
	}

	private fun onDislikeClicked(postRef: DatabaseReference) {
		postRef.runTransaction(object : Transaction.Handler {
			override fun doTransaction(mutableData: MutableData): Transaction.Result {
				val p = mutableData.getValue(Post::class.java)
					?: return Transaction.success(mutableData)

				if (p.dislikes?.contains(uid) == true) {
					// Unlike the post and remove self from likes
					p.dislike = p.dislike?.minus(1)
					p.dislikes?.remove(uid)
				} else {
					// Like the post and add self to likes
					p.dislike = p.dislike?.plus(1)
					if (uid != null) {
						p.dislikes?.add(uid)
					}
				}

				// Set value and report transaction success
				mutableData.value = p
				return Transaction.success(mutableData)
			}

			override fun onComplete(
				databaseError: DatabaseError?,
				committed: Boolean,
				currentData: DataSnapshot?
			) {
				// Transaction completed
			}
		})
	}

}