package com.example.login_signup_firebase

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.login_signup_firebase.databinding.ActivityHomeScreenBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class HomeScreen : AppCompatActivity() {
    private val binding: ActivityHomeScreenBinding by lazy { ActivityHomeScreenBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val currentUser: FirebaseUser? = auth.currentUser

        if (currentUser != null) {
            binding.textView6.text = currentUser.email
        }

        binding.button4.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginScreen::class.java))
            finish()
        }
    }
}