package com.example.event_viewer.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val location: String,
    val startDateTime: String,
    val endDateTime: String,
    val ageRange: String
)
