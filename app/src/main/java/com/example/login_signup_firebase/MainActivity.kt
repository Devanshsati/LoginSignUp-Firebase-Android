package com.example.login_signup_firebase

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.login_signup_firebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {ActivityMainBinding.inflate(layoutInflater)}
//    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val spannableString = SpannableString("Welcome")
        spannableString.setSpan(ForegroundColorSpan(Color.BLUE), 0, 4,0)
        binding.textView.text = spannableString

        binding.button1.setOnClickListener {
            startActivity(Intent(this, LoginScreen::class.java))
        }
        binding.button2.setOnClickListener {
            startActivity(Intent(this, RegisterScreen::class.java))
        }

//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
//        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }
}