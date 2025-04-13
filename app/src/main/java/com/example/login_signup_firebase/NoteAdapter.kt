package com.example.login_signup_firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.login_signup_firebase.databinding.NotesRvItemBinding

class NoteAdapter(val notes: MutableList<NoteItem>, private val deleteNote: (NoteItem) -> Unit) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NotesRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding, deleteNote)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NoteViewHolder(private val binding: NotesRvItemBinding, private val deleteNote: (NoteItem) -> Unit ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteItem){
            binding.textView8.text = note.title
            binding.textView9.text = note.description
            binding.imageView8.setOnClickListener {
                deleteNote(note)
            }
        }
    }
}
