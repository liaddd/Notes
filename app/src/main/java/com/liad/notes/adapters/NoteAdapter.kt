package com.liad.notes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liad.notes.R
import com.liad.notes.models.Note

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.MyViewHolder>() {

    private val notesList = mutableListOf<Note>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_item_list, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = notesList[position]

        holder.title.text = note.title
        holder.description.text = note.description
        holder.priority.text = note.priority.toString()
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun setNotes(newNotes: List<Note>) {
        this.notesList.clear()
        this.notesList.addAll(newNotes)
        notifyDataSetChanged()
    }

    fun getNoteAt(position: Int): Note {
        return notesList[position]
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title : TextView = itemView.findViewById(R.id.note_item_list_title)
        val description : TextView = itemView.findViewById(R.id.note_item_list_description)
        val priority : TextView = itemView.findViewById(R.id.note_item_list_priority)
    }
}
