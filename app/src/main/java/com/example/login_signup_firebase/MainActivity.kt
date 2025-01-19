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
    }
}