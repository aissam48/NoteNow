package com.note.android.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 2)
abstract class AppDataClass:RoomDatabase() {
    abstract fun noteDao() : NoteDao
}