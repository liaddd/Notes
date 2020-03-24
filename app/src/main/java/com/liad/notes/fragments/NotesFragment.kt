package com.liad.notes.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.climacell.statefulLiveData.core.StatefulData
import com.liad.notes.R
import com.liad.notes.activities.MainActivity
import com.liad.notes.adapters.NoteAdapter
import com.liad.notes.models.Note
import com.liad.notes.utils.Constants.Companion.NOTE_DESC
import com.liad.notes.utils.Constants.Companion.NOTE_ID
import com.liad.notes.utils.Constants.Companion.NOTE_PRIORITY
import com.liad.notes.utils.Constants.Companion.NOTE_TITLE
import com.liad.notes.utils.extensions.changeFragment
import com.liad.notes.utils.extensions.toast
import com.liad.notes.viewmodels.NoteViewModel
import kotlinx.android.synthetic.main.fragment_notes.*
import org.koin.android.ext.android.inject

class NotesFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance(): NotesFragment {
            return NotesFragment()
        }
    }


    private lateinit var recyclerView: RecyclerView
    private val noteAdapter = NoteAdapter().apply { listener = createAdapterListener() }
    private val noteViewModel: NoteViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as? MainActivity)?.let { it.supportActionBar?.show() }
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setObservers()
        setListeners()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fragment_notes_add_note_floating_button -> {
                addNote()
            }
        }
    }

    private fun addNote() {
        activity?.let {
            changeFragment(
                it.supportFragmentManager,
                R.id.main_activity_frame_layout,
                AddNoteFragment.newInstance(),
                true
            )
        }
    }

    private fun initViews() {
        recyclerView = fragment_notes_recycler_view
        recyclerView.apply {
            adapter = noteAdapter
            layoutManager = activity?.let { LinearLayoutManager(it, RecyclerView.VERTICAL, false) }
        }
    }

    private fun setObservers() {
        noteViewModel.getAllNotes().observe(viewLifecycleOwner, Observer {
            when (it) {
                is StatefulData.Success -> {
                    showProgress(false)
                    fragment_note_empty_state_text.visibility = View.GONE
                    if (it.data.isNullOrEmpty()) fragment_note_empty_state_text.visibility =
                        View.VISIBLE
                    noteAdapter.setNotes(it.data)
                }
                is StatefulData.Loading -> {
                    showProgress()
                }
                is StatefulData.Error -> {
                    showProgress()
                }
            }
        })
    }

    private fun setListeners() {
        fragment_notes_add_note_floating_button.setOnClickListener(this)

        val itemTouchHelper = ItemTouchHelper(getItemTouchHelper())
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun getItemTouchHelper() =
        object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT /*or ItemTouchHelper.LEFT*/) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.RIGHT) {
                    val currentNote = noteAdapter.getNoteAt(viewHolder.adapterPosition)
                    activity?.let {
                        toast(
                            it,
                            "${currentNote.title} removed!"
                        )
                    }
                    noteViewModel.deleteNote(currentNote)
                }
            }
        }

    private fun showProgress(show: Boolean = true) {
        fragment_note_progress_bar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun createAdapterListener() =
        object : NoteAdapter.OnNoteClickListener {
            override fun onClick(note: Note) {
                activity?.let {
                    val bundle = Bundle()
                    bundle.putLong(NOTE_ID, note.id)
                    bundle.putString(NOTE_TITLE, note.title)
                    bundle.putString(NOTE_DESC, note.description)
                    bundle.putInt(NOTE_PRIORITY, note.priority)

                    changeFragment(
                        it.supportFragmentManager,
                        fragment = AddNoteFragment.newInstance(bundle),
                        addToBackStack = true
                    )
                }
            }
        }

}
