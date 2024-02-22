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
package com.example.juicetracker.ui.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.juicetracker.R
import com.example.juicetracker.data.Juice
import com.example.juicetracker.data.JuiceColor
import com.example.juicetracker.ui.JuiceTrackerViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryBottomSheet(
    juiceTrackerViewModel: JuiceTrackerViewModel,
    sheetScaffoldState: BottomSheetScaffoldState,
    onCancel: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val juice by juiceTrackerViewModel.currentJuiceStream.collectAsState()

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = sheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Column {
                SheetHeader(Modifier.padding(dimensionResource(R.dimen.padding_small)))
                SheetForm(
                    juice = juice,
                    onUpdateJuice = juiceTrackerViewModel::updateCurrentJuice,
                    onCancel = onCancel,
                    onSubmit = onSubmit,
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(R.dimen.padding_medium)
                    )
                )
            }
        }
    ) {
        content()
    }
}

@Composable
fun SheetHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
            text = stringResource(R.string.bottom_sheet_headline),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Divider()
    }
}

@Composable
fun SheetForm(
    juice: Juice,
    onUpdateJuice: (Juice) -> Unit,
    onCancel: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TextInputRow(
            inputLabel = stringResource(R.string.juice_name),
            fieldValue = juice.name,
            onValueChange = { name -> onUpdateJuice(juice.copy(name = name)) },
            modifier = Modifier.fillMaxWidth()
        )
        TextInputRow(
            inputLabel = stringResource(R.string.juice_description),
            fieldValue = juice.description,
            onValueChange = { description -> onUpdateJuice(juice.copy(description = description)) },
            modifier = Modifier.fillMaxWidth()
        )
        ColorSpinnerRow(
            colorSpinnerPosition = findColorIndex(juice.color),
            onColorChange = { color ->
                onUpdateJuice(juice.copy(color = JuiceColor.values()[color].name))
            }
        )
        RatingInputRow(
            rating = juice.rating,
            onRatingChange = { rating -> onUpdateJuice(juice.copy(rating = rating)) }
        )
        ButtonRow(
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = dimensionResource(R.dimen.padding_medium)),
            onCancel = onCancel,
            onSubmit = onSubmit,
            submitButtonEnabled = juice.name.isNotEmpty()
        )
    }
}

@Composable
fun ButtonRow(
    onCancel: () -> Unit,
    onSubmit: () -> Unit,
    submitButtonEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            border = null
        ) {
            Text(stringResource(android.R.string.cancel).uppercase(Locale.getDefault()))
        }
        Button(
            onClick = onSubmit,
            enabled = submitButtonEnabled
        ) {
            Text(stringResource(R.string.save).uppercase(Locale.getDefault()))
        }
    }
}

@Composable
fun TextInputRow(
    inputLabel: String,
    fieldValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    InputRow(inputLabel, modifier) {
        TextField(
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small)),
            value = fieldValue,
            onValueChange = onValueChange,
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.surface,
                unfocusedContainerColor = colorScheme.surface,
                disabledContainerColor = colorScheme.surface,
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
    }
}

@Composable
fun InputRow(
    inputLabel: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = inputLabel,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier.weight(1f)
        )
        Box(modifier = Modifier.weight(2f)) {
            content()
        }
    }
}

private fun findColorIndex(color: String): Int {
    val juiceColor = JuiceColor.valueOf(color)
    return JuiceColor.values().indexOf(juiceColor)
}
