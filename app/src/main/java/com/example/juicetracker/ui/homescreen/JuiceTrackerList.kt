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
package com.example.juicetracker.ui.homescreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.juicetracker.R
import com.example.juicetracker.data.Juice

@Composable
fun JuiceTrackerList(
    juices: List<Juice>,
    onDelete: (Juice) -> Unit,
    onUpdate: (Juice) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(items = juices) { juice ->
            JuiceTrackerListItem(juice, onDelete, onUpdate)
        }
    }
}

@Composable
fun JuiceTrackerListItem(
    juice: Juice,
    onDelete: (Juice) -> Unit,
    onUpdate: (Juice) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onUpdate(juice)
            }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        JuiceIcon(juice.color)
        JuiceDetails(juice, modifier.weight(1f))
        DeleteButton(
            onDelete = {
                onDelete(juice)
            },
            modifier = Modifier.align(Alignment.Top)
        )
    }
}

@Composable
fun JuiceDetails(juice: Juice, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.Top) {
        Text(
            text = juice.name,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        )
        Text(juice.description)
        RatingDisplay(rating = juice.rating, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun DeleteButton(onDelete: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = { onDelete() },
    ) {
        Icon(
            modifier = modifier,
            painter = painterResource(R.drawable.delete_icon),
            contentDescription = stringResource(R.string.delete)
        )
    }
}

@Preview
@Composable
fun JuiceTrackerListPreview() {
    MaterialTheme {
        JuiceTrackerList(
            juices = listOf(
                Juice(1, "Mango", "Yummy!", "Yellow", 5),
                Juice(2, "Orange", "Refreshing~", "Orange", 4),
                Juice(3, "Grape", "Refreshing~", "Magenta", 2),
                Juice(4, "Celery", "Refreshing~", "Green", 1),
                Juice(5, "ABC", "Refreshing~", "Red", 4)
            ),
            onDelete = {},
            onUpdate = {})
    }
}
