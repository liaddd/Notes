package com.liad.notes.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import co.climacell.statefulLiveData.core.*
import com.liad.notes.database.NoteDao
import com.liad.notes.database.NoteDatabase
import com.liad.notes.models.Note
import java.util.concurrent.Executors

class NoteRepository(notesDatabase: NoteDatabase) {

    private val executor = Executors.newSingleThreadExecutor()
    val notesStatefulLiveData: StatefulLiveData<List<Note>>
    private val notes = mutableListOf<Note>()
    private val dao: NoteDao

    init {
        Log.i("Liad", "NoteRepository initialized $this")
        dao = notesDatabase.dao()

        notesStatefulLiveData = getAllNotes()
    }

    private fun getAllNotes(): StatefulLiveData<List<Note>> {
        val tempNotes = MutableStatefulLiveData<List<Note>>()
        tempNotes.putLoading()

        getNotesFromDb().observeForever { dbNotes ->
            if (dbNotes.isNullOrEmpty()) {
                Log.d("Liad", "Couldn't find any notes in Database")
                tempNotes.putData(dbNotes)
            } else {
                tempNotes.putData(dbNotes)
            }
        }
        return tempNotes
    }

    private fun getNotesFromDb(): LiveData<List<Note>> = dao.getAllNotes()


    fun insertNote(newNote: Note) {
        var possibleNoteList =
            (notesStatefulLiveData.value as? StatefulData.Success<List<Note>>)?.data
        val noteMutableLiveData = notesStatefulLiveData as? MutableStatefulLiveData<List<Note>>
        noteMutableLiveData?.putLoading()

        if (!possibleNoteList.isNullOrEmpty()) {
            possibleNoteList = possibleNoteList.toMutableList()
            possibleNoteList.add(newNote)
            noteMutableLiveData?.putData(possibleNoteList)
        } else {
            notes.add(newNote)
            noteMutableLiveData?.putData(notes)
        }
        saveNoteInDatabase(newNote)
    }

    private fun saveNoteInDatabase(note: Note) {
        executor.submit {
            val value = dao.insertNote(note)
            Log.d("Liad", "inserted value = $value")
        }
    }

    fun deleteAllNotes() {
        val notesMutableLiveData = notesStatefulLiveData as? MutableStatefulLiveData<List<Note>>
        notesMutableLiveData?.putLoading()
        executor.submit {
            try {
                dao.deleteAllNotes()
            } catch (exception: Exception) {
                Log.d("Liad", "error $exception")
            } finally {
                notesMutableLiveData?.putData(emptyList())
            }
        }
    }

    fun deleteNote(note: Note) {
        executor.submit {
            dao.deleteNote(note)
        }
    }
}