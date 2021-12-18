package com.note.android

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.note.android.room.AppDataClass
import com.note.android.room.Note
import kotlinx.android.synthetic.main.activity_add_note.*
import java.text.SimpleDateFormat
import java.util.*

class AddNote : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)


        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        back_.setOnClickListener {
            finish()
        }

        val room = Room.databaseBuilder(this, AppDataClass::class.java, "Notes").allowMainThreadQueries().build()
        val db = room.noteDao()

        val saveType = intent.getStringExtra("SaveType")

        when(saveType){

            "SaveEdit"->{
                val uid_ = intent.getStringExtra("Uid")

                val note = db.getNotes().filter {
                    it.uid == uid_
                }[0]

                input_title.setText(note.title)
                input_description.setText(note.description)

                save.visibility = View.GONE
                save_edit.visibility = View.GONE
                add_note_favoret.visibility = View.VISIBLE
                add_note_delete.visibility = View.VISIBLE
                add_note_edit.visibility = View.VISIBLE


                add_note_edit.setOnClickListener {
                    save_edit.visibility = View.VISIBLE
                    add_note_edit.visibility = View.GONE
                    input_title.isEnabled = true
                    input_description.isEnabled = true
                    input_description.requestFocus()
                }

                add_note_delete.setOnClickListener {

                    db.deleteNote(note)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }

                when(note.type){
                    ""->{
                        add_note_favoret.visibility = View.VISIBLE
                        add_note_favoret_already.visibility = View.GONE
                        add_note_favoret.setOnClickListener {
                            note.type = "Favorite"
                            db.updateNote(note)
                            add_note_favoret.visibility = View.INVISIBLE
                            add_note_favoret.isEnabled = false
                            add_note_favoret_already.visibility  =View.VISIBLE
                        }

                        add_note_favoret_already.setOnClickListener {
                                add_note_favoret_already.visibility = View.GONE
                                add_note_favoret.visibility = View.VISIBLE
                                add_note_favoret.isEnabled = true
                                note.type = ""
                                db.updateNote(note)
                            }
                    }

                    "Favorite"->{
                        add_note_favoret.visibility = View.INVISIBLE
                        add_note_favoret_already.visibility = View.VISIBLE
                        add_note_favoret.isEnabled = false
                        add_note_favoret_already.setOnClickListener {
                            add_note_favoret_already.visibility = View.GONE
                            add_note_favoret.visibility = View.VISIBLE
                            add_note_favoret.isEnabled = true
                            note.type = ""
                            db.updateNote(note)

                        }

                        add_note_favoret.setOnClickListener {
                            note.type = "Favorite"
                            db.updateNote(note)
                            add_note_favoret.visibility = View.INVISIBLE
                            add_note_favoret.isEnabled = false
                            add_note_favoret_already.visibility = View.VISIBLE
                        }

                    }
                }



                save_edit.setOnClickListener {

                    if(input_description.text.isNotEmpty()){
                        note.description = input_description.text.toString()
                    }

                    if(input_title.text.isNotEmpty()){
                        note.title = input_title.text.toString()
                    }



                    val date = Date().time
                    val timeEdit = SimpleDateFormat("yyyy-MM-dd HH-mm").format(date)

                    note.timeEdit = timeEdit

                    db.updateNote(note)

                    save_edit.visibility = View.GONE
                    add_note_edit.visibility = View.VISIBLE

                    input_title.isEnabled = false
                    input_description.isEnabled = false

                }
            }

            "Save"->{
                input_title.isEnabled = true
                input_description.isEnabled = true
                save.visibility = View.VISIBLE
                save_edit.visibility = View.GONE
                save.setOnClickListener {
                    if (input_title.text.isEmpty()){
                        Toast.makeText(this, "Add title", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    if (input_description.text.isEmpty()){
                        Toast.makeText(this, "Add description", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val date = Date().time
                    val uid = UUID.randomUUID().toString()
                    val time = SimpleDateFormat("yyyy-MM-dd HH-mm").format(date)
                    val openTimes = 1
                    val title = input_title.text.toString()
                    val description = input_description.text.toString()
                    val timeEdit = time
                    val note  = Note(uid, title, description, time, timeEdit, openTimes, "")

                    db.insertNote(note)

                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                }

            }
        }




    }
}