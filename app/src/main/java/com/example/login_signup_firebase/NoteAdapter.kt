package com.example.login_signup_firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.login_signup_firebase.databinding.NotesRvItemBinding

class NoteAdapter(
    private val notes: List<NoteItem>,
    private val onDelete: (NoteItem) -> Unit,
    private val onUpdate: (NoteItem) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(val binding: NotesRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NotesRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.textView8.text = note.title
        holder.binding.textView9.text = note.description

        holder.binding.imageView8.setOnClickListener {
            onDelete(note)
        }

        holder.binding.imageView9.setOnClickListener {
            onUpdate(note)
        }
    }
}

