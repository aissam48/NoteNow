package com.note.android

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.note.android.groupie.GroupyAdapter
import com.note.android.room.AppDataClass
import com.note.android.room.Note
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_bottom.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var section :Section
    private lateinit var sectionThis: Section
    private lateinit var groupie : GroupAdapter<GroupieViewHolder>
    private lateinit var allNotes : MutableList<Note>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        add_note.setOnClickListener{
            val intent = Intent(this, AddNote::class.java)
            intent.putExtra("Title","")
            intent.putExtra("Description","")
            intent.putExtra("Time","")
            intent.putExtra("SaveType", "Save")
            intent.putExtra("Uid", "")
            startActivity(intent)
        }

        sreach.setOnClickListener {
            val intent = Intent(this, SearchNotes::class.java)
            startActivity(intent)
        }

        val room = Room.databaseBuilder(this, AppDataClass::class.java, "Notes").fallbackToDestructiveMigration()
            .allowMainThreadQueries().build()

        val db = room.noteDao()
        allNotes = db.getNotes()

        allNotes.sortByDescending {
            it.timeEdit
        }

        section = Section()
        groupie = GroupAdapter<GroupieViewHolder>()
        groupie.add(section)
        recycler_notes.adapter = groupie

        for (i in allNotes){
            val item = GroupyAdapter(i , this)
            section.add(item)
            groupie.notifyDataSetChanged()
        }

        val inflater = LayoutInflater.from(this).inflate(R.layout.layout_bottom, null)

        more_options.setOnClickListener {

            if(inflater.parent != null){
                (inflater.parent as ViewGroup).removeView(inflater)
            }

            val bottomSheetDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
            bottomSheetDialog.setContentView(inflater)

            bottomSheetDialog.create()

            inflater.all_notes_.setOnClickListener {

                section.clear()
                for (i in allNotes){
                    section.add(GroupyAdapter(i, this))
                    groupie.notifyDataSetChanged()
                }

                bottomSheetDialog.dismiss()
                type_.text = "All Notes"
            }

            inflater.favorites_.setOnClickListener {

                section.clear()

                val favorites = allNotes.filter {
                    it.type == "Favorite"
                }

                for (i in favorites){
                    section.add(GroupyAdapter(i, this))
                    groupie.notifyDataSetChanged()
                }
                bottomSheetDialog.dismiss()
                type_.text = "Favorites"

            }

            bottomSheetDialog.show()
        }



    }

    override fun onStart() {
        super.onStart()

        val room = Room.databaseBuilder(this, AppDataClass::class.java, "Notes").fallbackToDestructiveMigration()
            .allowMainThreadQueries().build()

        val db = room.noteDao()
        allNotes = db.getNotes()

        allNotes.sortByDescending {
            it.timeEdit
        }

        section.clear()

        for (i in allNotes){
            val item = GroupyAdapter(i , this)
            section.add(item)
            groupie.notifyDataSetChanged()
        }

        type_.text = "All Notes"

    }
}