package com.liad.notes.di

import com.liad.notes.NoteApplication
import com.liad.notes.database.NoteDatabase
import com.liad.notes.repositories.NoteRepository
import com.liad.notes.viewmodels.NoteViewModel
import org.koin.dsl.module

val appModule = module {


    single { NoteDatabase.getDatabase(NoteApplication.instance) }
    single { NoteRepository(get()) }


    factory { NoteViewModel(get()) }
}