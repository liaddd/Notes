package com.liad.notes.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes")
data class Note(val title: String, val description: String, val priority: Int) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}