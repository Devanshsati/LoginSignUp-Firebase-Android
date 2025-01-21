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
//    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
//    private var showOneTapUI = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth = Firebase.auth


//        val signInRequest = BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId(getString(R.string.default_web_client_id))
//                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(true)
//                    .build()
//            )
//            .build()



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

//        binding.imageView10.setOnClickListener {
//            val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
//            val idToken = googleCredential.googleIdToken
//            when {
//                idToken != null -> {
//                    // Got an ID token from Google. Use it to authenticate
//                    // with Firebase.
//                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
//                    auth.signInWithCredential(firebaseCredential)
//                        .addOnCompleteListener(this) { task ->
//                            if (task.isSuccessful) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d("TAG", "signInWithCredential:success")
//                                val user = auth.currentUser
////                                updateUI(user)
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w("TAG", "signInWithCredential:failure", task.exception)
////                                updateUI(null)
//                            }
//                        }
//                }
//                else -> {
//                    // Shouldn't happen.
//                    Log.d("TAG", "No ID token!")
//                }
//            }
//        }

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


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            REQ_ONE_TAP -> {
//                try {
//                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
//                    val idToken = credential.googleIdToken
//                    when {
//                        idToken != null -> {
//                            // Got an ID token from Google. Use it to authenticate
//                            // with Firebase.
//                            Log.d("TAG", "Got ID token.")
//                        }
//                        else -> {
//                            // Shouldn't happen.
//                            Log.d("TAG", "No ID token!")
//                        }
//                    }
//                } catch (e: ApiException) {
//                    Log.d("TAG", e.localizedMessage))
//
//                }
//            }
//        }
//    }
}