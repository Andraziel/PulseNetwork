package fr.isen.pulse.pulsenetwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import fr.isen.pulse.pulsenetwork.classes.Commentaire
import fr.isen.pulse.pulsenetwork.classes.Post
import fr.isen.pulse.pulsenetwork.classes.UserInfo
import fr.isen.pulse.pulsenetwork.databinding.ActivityNewCommBinding
import fr.isen.pulse.pulsenetwork.databinding.ActivityPostBinding

class NewCommActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewCommBinding
    private lateinit var idPost: String
    private lateinit var post: Post
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var fullName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewCommBinding.inflate(layoutInflater)
        setContentView(binding.root)

        post = intent.getSerializableExtra("post") as Post

        idPost = post.id.toString()

        // Initiate the listener for the close button
        binding.closeCommentaire.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)

            intent.putExtra("post",post)

            startActivity(intent)
        }

        // Getting the user name from the database
        val link = Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app").getReference("pulse/users/$uid")
        link.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<UserInfo>()
                fullName = user?.firstName + " " + user?.lastName
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

        // Initiate the listener for the validate button
        binding.valideCommentaire.setOnClickListener {
            val commentaire = binding.commentairePost.text.toString()

            val database = Firebase.database("https://pulsenetwork-d6541-default-rtdb.europe-west1.firebasedatabase.app")
            val myRef = database.getReference("pulse/posts/$idPost/commentaries")
            val id = myRef.push().key
            val comm = Commentaire(fullName,commentaire,id)
            id?.let {
                myRef.child(it).setValue(comm)
            }

            val intent = Intent(this, DetailActivity::class.java)

            intent.putExtra("post",post)

            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionPosts -> {
                // Open FeedActivity
                val intent = Intent(this, FeedActivity::class.java)
                startActivity(intent)
                return true
            }
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