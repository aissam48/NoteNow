package com.note.android.groupie

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.room.Room
import com.note.android.AddNote
import com.note.android.R
import com.note.android.room.AppDataClass
import com.note.android.room.Note
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_note.*

class GroupyAdapter(val note : Note, private val context:Context) : Item(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.title_item.text = note.title
        viewHolder.content_item.text = note.description
        viewHolder.time_item.text = note.time

        when(note.type){

            ""->{
                viewHolder.fav_this.visibility = View.GONE
            }

            "Favorite"->{
                viewHolder.fav_this.visibility = View.VISIBLE
            }

        }

        viewHolder.delete_item.setOnClickListener {

            val room = Room.databaseBuilder(context, AppDataClass::class.java, "Notes").allowMainThreadQueries().build()
            val db = room.noteDao()
            db.deleteNote(note)

            viewHolder.constraint_item.visibility = View.GONE

        }

        viewHolder.itemView.setOnClickListener {
            val intent  = Intent(context, AddNote::class.java)
            intent.putExtra("Title", note.title)
            intent.putExtra("Description", note.description)
            intent.putExtra("Time", note.time)
            intent.putExtra("SaveType", "SaveEdit")
            intent.putExtra("Uid", note.uid)
            context.startActivity(intent)
        }
    }

    override fun getLayout(): Int = R.layout.item_note
}