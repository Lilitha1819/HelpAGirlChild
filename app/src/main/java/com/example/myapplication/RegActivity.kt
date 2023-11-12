package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        val button = findViewById<View>(R.id.signup)
        val login = findViewById<View>(R.id.login)

        login.setOnClickListener {
            startActivity(Intent(this@RegActivity, LoginActivity::class.java))
        }

        button.setOnClickListener {
            val emailView = findViewById<View>(R.id.email) as TextView
            val passwordView = findViewById<View>(R.id.password) as TextView
            val nameView = findViewById<View>(R.id.user_name) as TextView

            val email = emailView.text.toString()
            val password = passwordView.text.toString()
            val name = nameView.text.toString()

            if (email.isEmpty()) {
                emailView.error = "Empty field!"
            } else if (password.isEmpty()) {
                passwordView.error = "Empty field!"
            } else if (name.isEmpty()) {
                nameView.error = "Empty field!"
            } else {
                signup(email, password, name)
            }
        }
    }

    private fun signup(email : String, password : String, name : String) {
        auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    insertUserData(task.result.user?.uid, name)
                } else {
                    Toast.makeText(this@RegActivity, "Failed to register", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun insertUserData(id: String?, name: String) {
        val user = hashMapOf(
            "id" to id,
            "name" to name,
            "image" to R.drawable.avatar_1
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("users").add(user)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Successful registration", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext,LoginActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Failed to add user", Toast.LENGTH_LONG).show()
            }
    }
}