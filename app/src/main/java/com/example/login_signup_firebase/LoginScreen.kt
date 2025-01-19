package com.example.login_signup_firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.login_signup_firebase.databinding.ActivityLoginScreenBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginScreen : AppCompatActivity() {
    private val binding: ActivityLoginScreenBinding by lazy { ActivityLoginScreenBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth = Firebase.auth


        binding.button.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithEmail:success")
                        //val user = auth.currentUser
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        // startActivity(Intent(this, Homepage::class.java)
                        // finish()
                    }
                }
            }
        }
        binding.textView2.setOnClickListener {// startActivity(Intent(this, ForgotPasswordScreen::class.java))}
            binding.textView3.setOnClickListener {
                startActivity(Intent(this, RegisterScreen::class.java))
                finish()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if (auth.currentUser != null) {
            // startActivity(Intent(this, Homepage::class.java))
            Toast.makeText(this,"Already Logged In", Toast.LENGTH_SHORT).show()
            Log.d("TAG", "onStart: Already Logged In")
            Firebase.auth.signOut()
            Toast.makeText(this,"Logged Out", Toast.LENGTH_SHORT).show()
            Log.d("TAG", "onStart: Logged Out")
        }
    }
}