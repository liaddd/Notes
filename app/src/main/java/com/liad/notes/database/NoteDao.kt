package com.liad.notes.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.liad.notes.models.Note


@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Query("SELECT * FROM notes ORDER BY priority DESC")
    fun getAllNotes() : LiveData<List<Note>>

    @Delete
    fun deleteNote(note : Note)

    @Query("DELETE FROM notes")
    fun deleteAllNotes()
}