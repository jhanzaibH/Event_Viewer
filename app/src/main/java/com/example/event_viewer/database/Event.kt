package com.example.event_viewer.database


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val location: String,
    val startDateTime: Date,
    val endDateTime: Date,
    val ageRange: String
)
