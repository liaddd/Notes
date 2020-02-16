package com.liad.notes.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.liad.notes.R
import com.liad.notes.models.Note
import com.liad.notes.utils.extensions.toast
import com.liad.notes.viewmodels.NoteViewModel
import kotlinx.android.synthetic.main.fragment_add_note.*
import org.koin.android.ext.android.inject


class AddNoteFragment : Fragment() {

    companion object {
        fun newInstance(): AddNoteFragment {
            return AddNoteFragment()
        }
    }

    private lateinit var titleTextInputLayout: TextInputLayout
    private lateinit var descriptionTextInputLayout: TextInputLayout
    private lateinit var numberPicker: NumberPicker

    private val noteViewModel: NoteViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_add_note, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
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
            noteViewModel.insertNote(Note(title, description, priority))
            activity?.supportFragmentManager?.popBackStack()
        } else {
            activity?.let { toast(it, "Please make sure you've filled all details...") }
        }
    }

    private fun validateFields(): Boolean {
        return !titleTextInputLayout.editText?.text.isNullOrBlank() && !descriptionTextInputLayout.editText?.text.isNullOrBlank()
    }
}
