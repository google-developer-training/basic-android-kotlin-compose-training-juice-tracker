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

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JuiceDao {
    @Query("SELECT * FROM juice")
    fun getAll(): Flow<List<Juice>>

    @Query("SELECT * FROM juice WHERE id = :id")
    fun get(id: Long): Flow<Juice>

    @Insert
    suspend fun insert(juice: Juice)

    @Delete
    suspend fun delete(juice: Juice)

    @Update
    suspend fun update(juice: Juice)
}
