package fr.isen.pulse.pulsenetwork

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.pulse.pulsenetwork.classes.Post

class FeedAdapter(private var posts: ArrayList<Post>, val OnClick: (name: Post) -> Unit) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
	class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view){
		val title = view.findViewById<TextView>(R.id.auteurView)
		val description = view.findViewById<TextView>(R.id.commentaireView)
		val like = view.findViewById<TextView>(R.id.likeView)
		val dislike = view.findViewById<TextView>(R.id.dislikeView)
		val image = view.findViewById<ImageView>(R.id.imageView)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.post_cell, parent, false)

		return FeedViewHolder(view)
	}

	override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
		val post = posts[position]

		holder.title.text = post.titre
		holder.description.text = post.description
		holder.like.text = post.like.toString()
		holder.dislike.text = post.dislike.toString()

		val image = post.image
		if (image!= "") { if (image != null) {
			Picasso.get().load(image).into(holder.image) }}


		holder.itemView.setOnClickListener {
			OnClick(post)
		}

	}

	override fun getItemCount(): Int = posts.size

	fun refresh(Newposts:ArrayList<Post>) {
		posts = Newposts
		notifyDataSetChanged()
	}
}
