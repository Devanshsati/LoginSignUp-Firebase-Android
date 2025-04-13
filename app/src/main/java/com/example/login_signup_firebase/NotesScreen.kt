package com.example.login_signup_firebase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.login_signup_firebase.databinding.ActivityNotesScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotesScreen : AppCompatActivity() {
    private val binding: ActivityNotesScreenBinding by lazy { ActivityNotesScreenBinding.inflate(layoutInflater) }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private val noteKeyMap = mutableMapOf<NoteItem, String>() // Track notes with their Firebase keys
    private val currentUser: FirebaseUser? by lazy { auth.currentUser }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance("https://login-register-firebase-b15d4-default-rtdb.europe-west1.firebasedatabase.app/").reference
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            reverseLayout = true
            stackFromEnd = true
        }

        currentUser?.let { user ->
            val userId = user.uid
            val notesRef = databaseReference.child("USERS").child(userId).child("notes")

            notesRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val noteList = mutableListOf<NoteItem>()
                    noteKeyMap.clear()

                    for (noteSnapshot in snapshot.children) {
                        val note = noteSnapshot.getValue(NoteItem::class.java)
                        note?.let {
                            noteList.add(it)
                            noteKeyMap[it] = noteSnapshot.key ?: ""
                        }
                    }

                    noteAdapter = NoteAdapter(noteList) { noteToDelete ->
                        val noteKey = noteKeyMap[noteToDelete]
                        noteKey?.let {
                            notesRef.child(it).removeValue().addOnSuccessListener {
                                noteAdapter.notes.remove(noteToDelete)
                                "Note deleted".showToast()
                            }.addOnFailureListener {
                                "Failed to delete note".showToast()
                            }
                        }
                    }

                    recyclerView.adapter = noteAdapter
                }

                override fun onCancelled(error: DatabaseError) {
                    "Error: ${error.message}".showToast()
                }
            })
        }

        binding.button6.setOnClickListener {
            val title = binding.editTextText.text.toString()
            val description = binding.editTextText2.text.toString()

            if (title.isEmpty() && description.isEmpty()) {
                "Please fill the fields".showToast()
            } else {
                currentUser?.let { user ->
                    val userId = user.uid
                    val noteKey: String? = databaseReference.child("USERS").child(userId)
                        .child("notes").push().key
                    val noteItem = NoteItem(title, description)
                    noteKey?.let {
                        databaseReference.child("USERS").child(userId).child("notes").child(it)
                            .setValue(noteItem)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    "Save Successful".showToast()
                                    binding.editTextText.text.clear()
                                    binding.editTextText2.text.clear()
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