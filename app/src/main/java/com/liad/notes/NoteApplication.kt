package com.liad.notes

import android.app.Application
import com.liad.notes.di.appModule
import org.koin.core.context.startKoin

class NoteApplication : Application() {

    companion object {
        lateinit var instance: Application
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            modules(listOf(appModule))
        }
    }


}