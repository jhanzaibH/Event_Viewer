package com.example.event_viewer.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EventDao {
    @Insert
    fun insertEvent(event: Event)

    @Query("SELECT * FROM Event")
    suspend fun getAllEvents(): List<Event>

    // ... other queries as needed
}
