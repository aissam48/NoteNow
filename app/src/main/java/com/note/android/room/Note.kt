package com.note.android.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Note(

    @PrimaryKey val uid:String,
    @ColumnInfo(name = "Title") var title:String,
    @ColumnInfo(name = "Description") var description :String,
    @ColumnInfo(name = "Time") val time :String,
    @ColumnInfo(name = "TimeEdit") var timeEdit :String,
    @ColumnInfo(name = "OpenTimes") val openTimes : Int,
    @ColumnInfo(name = "Type") var type : String

)