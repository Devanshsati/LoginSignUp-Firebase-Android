package com.example.login_signup_firebase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.login_signup_firebase.databinding.ActivityNotesScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NotesScreen : AppCompatActivity() {
    private val binding: ActivityNotesScreenBinding by lazy { ActivityNotesScreenBinding.inflate(layoutInflater) }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Correct database instance with the specific region
        databaseReference = FirebaseDatabase.getInstance("https://login-register-firebase-b15d4-default-rtdb.europe-west1.firebasedatabase.app/").reference
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser

        binding.button6.setOnClickListener {
            val title = binding.editTextText.text.toString()
            val description = binding.editTextText2.text.toString()

            if (title.isEmpty() && description.isEmpty()) {
                "Please fill the fields".showToast()
            } else {
                if (currentUser != null) {
                    val userId = currentUser.uid
                    val noteKey: String? = databaseReference.child("USERS").child(userId).child("notes").push().key
                    val noteItem = NoteItem(title, description)
                    if (noteKey != null) {
                        databaseReference.child("USERS ").child(userId).child("notes").child(noteKey)
                            .setValue(noteItem)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    "Save Successful".showToast()
                                } else {
                                    "Fail To Save".showToast()
                                }
                            }
                    }
                }
            }
        }
    }

    private fun String.showToast() {
        Toast.makeText(this@NotesScreen, this, Toast.LENGTH_SHORT).show()
    }
}
