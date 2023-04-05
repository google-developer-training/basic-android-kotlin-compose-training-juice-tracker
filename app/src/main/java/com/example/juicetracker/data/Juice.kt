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
package com.example.juicetracker.data

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.juicetracker.R
import com.example.juicetracker.ui.theme.Orange as OrangeColor

/**
 * Represents a single table in the database
 *
 * Note that the color type is different than in the Main branch.
 * Please remove app and re-install if existing app from the Main branch exist
 * to avoid database error
 */
@Entity
data class Juice(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val description: String = "",
    val color: String,
    val rating: Int
)

enum class JuiceColor(val color: Color, @StringRes val label: Int) {
    Red(Color.Red, R.string.red),
    Blue(Color.Blue, R.string.blue),
    Green(Color.Green, R.string.green),
    Cyan(Color.Cyan, R.string.cyan),
    Yellow(Color.Yellow, R.string.yellow),
    Magenta(Color.Magenta, R.string.magenta),
    Orange(OrangeColor, R.string.orange)
}
