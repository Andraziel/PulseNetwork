package fr.isen.pulse.pulsenetwork

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.util.Strings
import com.squareup.picasso.Picasso

class FeedAdapter(private var posts: ArrayList<Strings>, val OnClick: (name: Strings) -> Unit) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
    class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title = view.findViewById<TextView>(R.id.titleView)
        val description = view.findViewById<TextView>(R.id.descriptionView)
        val image = view.findViewById<ImageView>(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_cell, parent, false)

        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val post = posts[position]

        holder.title.text = ""
        holder.description.text = ""

        /*
        val image = post.image
        if (image != "") { Picasso.get().load(image).into(holder.image) }
        */

        holder.itemView.setOnClickListener {
            OnClick(post)
        }

    }

    override fun getItemCount(): Int = posts.size

}
