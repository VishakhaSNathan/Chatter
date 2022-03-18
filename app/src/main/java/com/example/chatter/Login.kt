package com.example.chatter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var password : EditText
    lateinit var login: Button
    lateinit var signup: Button
    lateinit var Auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //initialise the values
        password = findViewById(R.id.et_password)
        email = findViewById(R.id.et_useremail)
        login = findViewById(R.id.btn_login)
        signup = findViewById(R.id.btn_signup)
        Auth = FirebaseAuth.getInstance()

        //signup logic
        signup.setOnClickListener {
           val intent = Intent(this,Signup::class.java)
            startActivity(intent)
        }

        //login logic
        login.setOnClickListener {
            if(email.text.isNotEmpty() && password.text.isNotEmpty())
            {
                Auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this,MainActivity::class.java)
                            finish()
                            startActivity(intent)
                        } else {
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else
            {
                Toast.makeText(this@Login,"Login Failed",Toast.LENGTH_LONG).show()
            }
        }
    }
}