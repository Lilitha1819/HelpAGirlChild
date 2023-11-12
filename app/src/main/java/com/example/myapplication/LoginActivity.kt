package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.data.User
import com.example.myapplication.data.UserState
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val button = findViewById<View>(R.id.login)
        val reg = findViewById<View>(R.id.signup)

        reg.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegActivity::class.java))
        }

        button.setOnClickListener {
            val emailView = findViewById<View>(R.id.email) as TextView
            val passwordView = findViewById<View>(R.id.password) as TextView

            val email = emailView.text.toString()
            val password = passwordView.text.toString()
            if (email.isEmpty()) {
                emailView.error = "Empty field!"
            } else if (password.isEmpty()) {
                passwordView.error = "Empty field!"
            } else {
                authenticate(email, password)
            }
        }
    }

    private fun authenticate(email : String, password : String) {
        auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {task ->
                if(task.isSuccessful) {
                    getNameOfUser(task)
                } else {
                    Toast.makeText(this@LoginActivity, "Incorrect credentials", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun getNameOfUser(results : Task<AuthResult>) {

        var user: User? = null
        val database = FirebaseFirestore.getInstance()
        val email: String? = results.result.user?.email
        val id: String? = results.result.user?.uid

        database.collection("users").whereEqualTo("id", id)
            .get().addOnCompleteListener {task ->
                if(task.isSuccessful) {
                    for (doc in task.result) {
                        UserState.getInstance()?.setUser(User(email, id, doc.data["name"].toString(),
                            doc.data["image"].toString().toInt(), doc.id))
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }
                } else {
                    Toast.makeText(applicationContext, "Internal server error", Toast.LENGTH_LONG).show()
                }
            }
    }
}