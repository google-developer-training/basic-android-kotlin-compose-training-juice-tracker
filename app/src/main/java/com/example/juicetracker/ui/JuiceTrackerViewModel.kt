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
package com.example.juicetracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.juicetracker.data.Juice
import com.example.juicetracker.data.JuiceColor
import com.example.juicetracker.data.JuiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * View Model which maintain states for [JuiceTrackerApp]
 */
class JuiceTrackerViewModel(private val juiceRepository: JuiceRepository) : ViewModel() {
    private val emptyJuice = Juice(0, "", "", JuiceColor.Red.name, 3)
    private val _currentJuiceStream = MutableStateFlow(emptyJuice)
    val currentJuiceStream: StateFlow<Juice> = _currentJuiceStream
    val juiceListStream: Flow<List<Juice>> = juiceRepository.juiceStream

    fun resetCurrentJuice() = _currentJuiceStream.update { emptyJuice }
    fun updateCurrentJuice(juice: Juice) = _currentJuiceStream.update { juice }

    fun saveJuice() = viewModelScope.launch {
        if (_currentJuiceStream.value.id > 0) {
            juiceRepository.updateJuice(_currentJuiceStream.value)
        } else {
            juiceRepository.addJuice(_currentJuiceStream.value)
        }
    }

    fun deleteJuice(juice: Juice) = viewModelScope.launch {
        juiceRepository.deleteJuice(juice)
    }
}
