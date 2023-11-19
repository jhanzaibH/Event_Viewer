package com.example.event_viewer.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Event::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    // If you have more entities and DAOs, declare them here
}
