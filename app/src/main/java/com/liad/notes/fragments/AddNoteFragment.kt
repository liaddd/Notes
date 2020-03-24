package com.liad.notes.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.liad.notes.R
import com.liad.notes.activities.MainActivity
import com.liad.notes.models.Note
import com.liad.notes.utils.Constants.Companion.NOTE_DESC
import com.liad.notes.utils.Constants.Companion.NOTE_ID
import com.liad.notes.utils.Constants.Companion.NOTE_PRIORITY
import com.liad.notes.utils.Constants.Companion.NOTE_TITLE
import com.liad.notes.utils.extensions.toast
import com.liad.notes.viewmodels.NoteViewModel
import kotlinx.android.synthetic.main.fragment_add_note.*
import org.koin.android.ext.android.inject


class AddNoteFragment : Fragment() {

    companion object {
        fun newInstance(bundle: Bundle? = null): AddNoteFragment {
            val addNoteFragment = AddNoteFragment()
            bundle?.let { addNoteFragment.arguments = it }
            return addNoteFragment
        }
    }

    private lateinit var titleTextInputLayout: TextInputLayout
    private lateinit var descriptionTextInputLayout: TextInputLayout
    private lateinit var numberPicker: NumberPicker

    private val noteViewModel: NoteViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as? MainActivity)?.let { it.supportActionBar?.hide() }
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        arguments?.let {
            populateFields(it)
        }
        setListeners()
    }

    private fun initViews() {
        titleTextInputLayout = fragment_add_note_title_text_input_layout
        descriptionTextInputLayout = fragment_add_note_description_text_input_layout
        numberPicker = fragment_add_note_number_picker
        numberPicker.apply {
            minValue = 1
            maxValue = 10
        }
    }

    private fun populateFields(bundle: Bundle) {
        fragment_add_note_button.text = getString(R.string.save)
        titleTextInputLayout.editText?.setText(bundle.getString(NOTE_TITLE, ""))
        descriptionTextInputLayout.editText?.setText(bundle.getString(NOTE_DESC, ""))
        numberPicker.value = bundle.getInt(NOTE_PRIORITY, 1)
    }

    private fun setListeners() {
        fragment_add_note_button.setOnClickListener {
            onAddNoteButtonClicked()
        }
    }

    private fun onAddNoteButtonClicked() {
        if (validateFields()) {
            val title = titleTextInputLayout.editText?.text.toString()
            val description = descriptionTextInputLayout.editText?.text.toString()
            val priority = numberPicker.value
            val note = Note(title, description, priority)
            arguments?.let { note.id = it.getLong(NOTE_ID) }
            noteViewModel.insertNote(note)
            activity?.supportFragmentManager?.popBackStack()
        } else {
            activity?.let { toast(it, "Please make sure you've filled all details...") }
        }
    }

    private fun validateFields(): Boolean {
        return !titleTextInputLayout.editText?.text.isNullOrBlank() && !descriptionTextInputLayout.editText?.text.isNullOrBlank()
    }
}
