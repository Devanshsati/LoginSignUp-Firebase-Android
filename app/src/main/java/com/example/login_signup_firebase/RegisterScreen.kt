package com.example.login_signup_firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.login_signup_firebase.databinding.ActivityRegisterScreenBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegisterScreen : AppCompatActivity() {
    private val binding: ActivityRegisterScreenBinding by lazy { ActivityRegisterScreenBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth = Firebase.auth

        binding.button3.setOnClickListener {
            val email = binding.editTextTextEmailAddress1.text.toString()
            val password = binding.editTextTextPassword1.text.toString()
            val repeatPassword = binding.editTextTextPassword2.text.toString()
            if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != repeatPassword) {
                Toast.makeText(this,"Password does not match ❌",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "createUserWithEmail:success")
//                        val user = auth.currentUser
                        startActivity(Intent(this, LoginScreen::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext,"Authentication failed.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.textView3.setOnClickListener {
            startActivity(Intent(this, LoginScreen::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            Firebase.auth.signOut()
            Toast.makeText(this,"Logged Out", Toast.LENGTH_SHORT).show()
            Log.d("TAG", "onStart: Logged Out")
        }
    }
}