package com.liad.notes.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.liad.notes.R
import com.liad.notes.fragments.NotesFragment
import com.liad.notes.utils.extensions.changeFragment
import com.liad.notes.utils.extensions.toast
import com.liad.notes.viewmodels.NoteViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val noteViewModel : NoteViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeFragment(supportFragmentManager, R.id.main_activity_frame_layout, NotesFragment.newInstance())
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_delete_all -> {
                deleteAllNotes()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun deleteAllNotes() {
        noteViewModel.deleteAllNotes()
        toast(this, "all Notes deleted")
    }
}
