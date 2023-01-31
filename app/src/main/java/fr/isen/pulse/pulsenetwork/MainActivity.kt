package fr.isen.pulse.pulsenetwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import fr.isen.pulse.pulsenetwork.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		Firebase.database.getReference("message").addValueEventListener(object: ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				val value = snapshot.getValue<String>()
				Log.d("TAG","Value is: " + value)
				findViewById<TextView>(R.id.textView).text = value
			}

			override fun onCancelled(error: DatabaseError) {
				Log.w("TAG", "Failed to read value.", error.toException())
			}

		})

		binding.redir.setOnClickListener {
			val intenti = Intent(this, SignUpActivity::class.java)
			startActivity(intenti)

		}

		binding.sub.setOnClickListener {
			val login= binding.SiEmail.text.toString()
			Log.w("AAAA", login)
			val password= binding.mdp.text.toString()
			val authent = Firebase.auth
			//baseutil.setValue(login)
			//basemdp.setValue(password)
			if(login.isEmpty() || password.isEmpty()){
				val toast = Toast.makeText(applicationContext,"Fields can not be empty ! You're crazy...",
					Toast.LENGTH_SHORT)
				toast.show()
			}else {
				authent.signInWithEmailAndPassword(login, password)
					.addOnCompleteListener(this) { task ->
						if (task.isSuccessful) {
							Log.w("TAG", "signInWithEmail:success")
							val user = authent.currentUser
							val intent = Intent(this, FeedActivity::class.java)
							startActivity(intent)
							//updateUI(user)
						} else {
							Log.w("TAG", "signInWithEmail:failure", task.exception)
							//Toast.makeText(baseContext, "Authentication failed.",
							//Toast.LENGTH_SHORT).show()
							//updateUI(null)
						}
					}
			}


		}

		/*val button = findViewById<Button>(R.id.sub).setOnClickListener {
			val database = Firebase.database
			val myRef = database.getReference("message")

			myRef.setValue("Hello, World!")
		}*/
	}


}