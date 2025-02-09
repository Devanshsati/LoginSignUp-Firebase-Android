package com.example.login_signup_firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.login_signup_firebase.databinding.ActivityLoginScreenBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginScreen : AppCompatActivity() {
    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        credentialManager = CredentialManager.create(this)

        setupUI()
        checkExistingUser()
    }

    private fun setupUI() {
        binding.button.setOnClickListener { handleEmailPasswordLogin() }
        binding.textView3.setOnClickListener { navigateToRegister() }
        binding.imageView5.setOnClickListener { initiateGoogleSignIn() }
    }

    private fun checkExistingUser() {
        if (auth.currentUser != null) {
            navigateToHome()
        }
    }

    private fun handleEmailPasswordLogin() {
        val email = binding.editTextTextEmailAddress.text.toString()
        val password = binding.editTextTextPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showToast("Please fill all the details")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navigateToHome()
                } else {
                    showToast("Authentication failed: ${task.exception?.message}")
                }
            }
    }

    private fun initiateGoogleSignIn() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.default_web_client_id))
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    context = this@LoginScreen,
                    request = request
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                when (e) {
                    is NoCredentialException ->
                        showToast("No credentials found. Please sign up first")
                    is GetCredentialCancellationException ->
                        Log.d("Login", "User canceled authentication")
                    else ->
                        showToast("Authentication failed: ${e.message}")
                }
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is PublicKeyCredential -> {
                // Handle passkey authentication if needed
                Log.d("LoginScreen", "Received passkey credential")
            }
            is PasswordCredential -> {
                // Handle password credential if needed
                Log.d("LoginScreen", "Received password credential")
            }
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    handleGoogleCredential(credential)
                } else {
                    Log.e("LoginScreen", "Unexpected custom credential type")
                }
            }
            else -> {
                Log.e("LoginScreen", "Unexpected credential type")
            }
        }
    }

    private fun handleGoogleCredential(credential: CustomCredential) {
        try {
            val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val authCredential = GoogleAuthProvider.getCredential(googleCredential.idToken, null)

            auth.signInWithCredential(authCredential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navigateToHome()
                    } else {
                        showToast("Google sign-in failed: ${task.exception?.message}")
                    }
                }
        } catch (e: Exception) {
            Log.e("LoginScreen", "Error processing Google credential", e)
            showToast("Error processing Google sign-in")
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterScreen::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}