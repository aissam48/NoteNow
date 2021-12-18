package com.note.android.room

import androidx.room.*


@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getNotes():MutableList<Note>

    @Insert
    fun insertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Update
    fun updateNote(note: Note)
}