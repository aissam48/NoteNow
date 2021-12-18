package com.note.android

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.room.Room
import com.note.android.groupie.GroupyAdapter
import com.note.android.room.AppDataClass
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.activity_search_notes.*

class SearchNotes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_notes)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)


        val room = Room.databaseBuilder(this, AppDataClass::class.java, "Notes").fallbackToDestructiveMigration()
            .allowMainThreadQueries().build()

        val db = room.noteDao()
        val allNotes = db.getNotes()

        back_search.setOnClickListener {
            finish()
        }


        search_.doOnTextChanged { text, start, before, count ->

            if(text!!.isEmpty()){
                recycler_search.adapter = null
                return@doOnTextChanged
            }

            val section = Section()
            val groupie =  GroupAdapter<GroupieViewHolder>()
            groupie.add(section)

            recycler_search.adapter = groupie

            val listNotes = allNotes.filter {
                it.title.contains(text!!)
            }

            for (i in listNotes){
                section.add(GroupyAdapter(i, this))
                groupie.notifyDataSetChanged()
            }

            groupie.setOnItemClickListener { item, view ->

                val intent = Intent(this, AddNote::class.java)
                intent.putExtra("SaveType", "SaveEdit")
                startActivity(intent)
            }
        }

    }
}