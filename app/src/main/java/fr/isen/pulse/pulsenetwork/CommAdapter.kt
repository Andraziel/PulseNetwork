package fr.isen.pulse.pulsenetwork

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.pulse.pulsenetwork.classes.Commentaire
import fr.isen.pulse.pulsenetwork.classes.Post

class CommAdapter(private var comms: ArrayList<Commentaire>) : RecyclerView.Adapter<CommAdapter.CommViewHolder>() {
    class CommViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val auteur = view.findViewById<TextView>(R.id.auteurView)
        val commentaire = view.findViewById<TextView>(R.id.commentaireView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.com_cell, parent, false)

        return CommViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommViewHolder, position: Int) {
        val comm = comms[position]

        holder.auteur.text = comm.auteur
        holder.commentaire.text = comm.commentaire
    }

    override fun getItemCount(): Int = comms.size

    fun refresh(Newposts:ArrayList<Commentaire>) {
        comms = Newposts
        notifyDataSetChanged()
    }
}
