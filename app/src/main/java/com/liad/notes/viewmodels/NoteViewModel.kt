package com.liad.notes.viewmodels

import androidx.lifecycle.ViewModel
import co.climacell.statefulLiveData.core.StatefulLiveData
import com.liad.notes.models.Note
import com.liad.notes.repositories.NoteRepository

class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel() {


    fun getAllNotes(): StatefulLiveData<List<Note>> = noteRepository.notesStatefulLiveData

    fun insertNote(note: Note) = noteRepository.insertNote(note)

    fun deleteAllNotes() = noteRepository.deleteAllNotes()

    fun deleteNote(note : Note) = noteRepository.deleteNote(note)
}
