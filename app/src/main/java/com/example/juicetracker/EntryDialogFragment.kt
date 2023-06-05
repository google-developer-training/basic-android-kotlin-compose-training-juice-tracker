/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.juicetracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.R.layout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.juicetracker.data.JuiceColor
import com.example.juicetracker.databinding.FragmentEntryDialogBinding
import com.example.juicetracker.ui.AppViewModelProvider
import com.example.juicetracker.ui.EntryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

/**
 * This dialog allows the user to enter information about a donut, either creating a new
 * entry or updating an existing one.
 */
class EntryDialogFragment : BottomSheetDialogFragment() {

    private val entryViewModel by viewModels<EntryViewModel> { AppViewModelProvider.Factory }
    var selectedColor: JuiceColor = JuiceColor.Red

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEntryDialogBinding.inflate(inflater, container, false).root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val colorLabelMap = JuiceColor.values().associateBy { getString(it.label) }
        val binding = FragmentEntryDialogBinding.bind(view)
        val args: EntryDialogFragmentArgs by navArgs()
        val juiceId = args.itemId

        // If we arrived here with an itemId of >= 0, then we are editing an existing item
        if (args.itemId > 0) {
            // Request to edit an existing item, whose id was passed in as an argument.
            // Retrieve that item and populate the UI with its details
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    entryViewModel.getJuiceStream(args.itemId).filterNotNull().collect { item ->
                        with(binding){
                            name.setText(item.name)
                            description.setText(item.description)
                            ratingBar.rating = item.rating.toFloat()
                            colorSpinner.setSelection(findColorIndex(item.color))
                        }
                    }
                }
            }
        }

        binding.name.doOnTextChanged { _, start, _, count ->
            // Enable Save button if the current text is longer than 3 characters
            binding.saveButton.isEnabled = (start+count) > 0
        }
        binding.colorSpinner.adapter = ArrayAdapter(
            requireContext(),
            layout.support_simple_spinner_dropdown_item,
            colorLabelMap.map { it.key }
        )
        binding.colorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                // Get the label of the selected item
                val selected = parent.getItemAtPosition(pos).toString()
                // Get the enum value from string
                selectedColor = colorLabelMap[selected] ?: selectedColor
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //if nothing is selected, assign the first color choice as the selectedColor
                selectedColor = JuiceColor.Red
            }
        }

        // When the user clicks the Save button, use the data here to either update
        // an existing item or create a new one
        binding.saveButton.setOnClickListener {
            entryViewModel.saveJuice(
                juiceId,
                binding.name.text.toString(),
                binding.description.text.toString(),
                selectedColor.name,
                binding.ratingBar.rating.toInt()
            )
            dismiss()
        }

        // User clicked the Cancel button; just exit the dialog without saving the data
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun findColorIndex(color: String): Int {
        val juiceColor = JuiceColor.valueOf(color)
        return JuiceColor.values().indexOf(juiceColor)
    }
}
