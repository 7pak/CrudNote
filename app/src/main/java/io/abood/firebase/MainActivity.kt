package io.abood.firebase

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import io.abood.firebase.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var ref:DatabaseReference
    lateinit var myNoteList:ArrayList<Note>
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title = "Notes"
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        myNoteList=ArrayList()
        val database = Firebase.database
         ref =database.getReference("Notes")

        binding.floatBtn.setOnClickListener {
            showAddNoteDialog()

        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("Fetching FCM registration token failed")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
        })

        }

    override fun onStart() {
        super.onStart()

        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                myNoteList.clear()
                for (n in snapshot.children){
                   val notes= n.getValue(Note::class.java)
                    myNoteList.add(0,notes!!)
                }


                val noteadap=NoteAdapter(this@MainActivity,myNoteList)
                binding.listvieww.adapter=noteadap


                binding.listvieww.onItemClickListener= AdapterView.OnItemClickListener { _, _, position, _ ->
                    val intent=Intent(this@MainActivity,MainActivity2::class.java)
                    val mynotes= myNoteList[position]

                    intent.putExtra("title",mynotes.title)
                    intent.putExtra("notes",mynotes.note)

                    startActivity(intent)


                }
                binding.listvieww.onItemLongClickListener= AdapterView.OnItemLongClickListener { _, _, position, _ ->

                   updateAndDeleteNote(position)

                    true
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,"error while reading the data",Toast.LENGTH_LONG).show()
            }

        })

    }



    private fun showAddNoteDialog(){
        val alertBuild=AlertDialog.Builder(this)
        val view=layoutInflater.inflate(R.layout.add_note,null)
        alertBuild.setView(view)
        alertBuild.setTitle("Add notes")
        val alertDialog=alertBuild.create()
        alertDialog.show()

        val addButton=view.findViewById<Button>(R.id.addButton)

        val title=view.findViewById<EditText>(R.id.editTitle).text
        val notes=view.findViewById<EditText>(R.id.editNote).text
        val id=ref.push().key

        val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val time = current.format(DateTimeFormatter.ofPattern("EEEE hh:mm a "))

        addButton.setOnClickListener {


            if (title.isNotEmpty() && notes.isNotEmpty()) {
                val noteData=Note(id!!,title.toString(),notes.toString(),time)
                ref.child(id).setValue(noteData)

                alertDialog.dismiss()
            }else Toast.makeText(this,"theres no title & note",Toast.LENGTH_LONG).show()
        }


    }

    private fun updateAndDeleteNote(position:Int){

        val alertBuild=AlertDialog.Builder(this)
        val view=layoutInflater.inflate(R.layout.update_delete_dlg,null)
        alertBuild.setView(view)
        alertBuild.setTitle("edit note")
        val alertDialog=alertBuild.create()
        alertDialog.show()

        val manote =myNoteList[position]
        view.findViewById<EditText>(R.id.title_upde).setText(manote.title)
        view.findViewById<EditText>(R.id.note_upde).setText(manote.note)

        view.findViewById<Button>(R.id.btn_update).setOnClickListener {
            val maref=ref.child(manote.id!!)
            val titlen= view.findViewById<EditText>(R.id.title_upde).text.toString()
            val notee= view.findViewById<EditText>(R.id.note_upde).text.toString()

            maref.setValue(Note(manote.id,titlen,notee,manote.time))

            alertDialog.dismiss()
        }


        view.findViewById<Button>(R.id.btn_delete).setOnClickListener {
            ref.child(manote.id!!).removeValue()

            alertDialog.dismiss()
        }



    }


}

