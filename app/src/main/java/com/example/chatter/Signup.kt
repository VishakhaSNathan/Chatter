package com.example.chatter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Signup : AppCompatActivity() {
    lateinit var Auth : FirebaseAuth
    lateinit var password : TextView
    lateinit var name : TextView
    lateinit var email : TextView
    lateinit var signup : Button
    lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        name = findViewById(R.id.et_username)
        password = findViewById(R.id.et_password)
        email = findViewById(R.id.et_email)
        signup = findViewById(R.id.btn_signup)
        Auth = FirebaseAuth.getInstance()


        signup.setOnClickListener {
           val emailId : String = email.text.toString()
           val signinPassword : String = password.text.toString()
           val username : String = name.text.toString()
            signupUser(username,emailId,signinPassword)
        }
    }

    private fun signupUser(name:String, email: String, password: String) {
        if(name.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
            Auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        addUsertoDatabase(name, email, Auth.currentUser?.uid!!)
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        else {
            Toast.makeText(
                baseContext, "Please provide all details",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun addUsertoDatabase(name: String, email: String, uid: String) {
        database = FirebaseDatabase.getInstance().getReference()
        database.child("user").child(uid).setValue(User(name,email,uid))
    }
}