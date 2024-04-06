package io.abood.firebase

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class NoteAdapter(context: Context,noteList:ArrayList<Note>):ArrayAdapter<Note>(context,0,noteList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view= LayoutInflater.from(context).inflate(R.layout.note_row,parent,false)
        val note: Note? =getItem(position)
        view.findViewById<TextView>(R.id.titleText).text=note?.title
        view.findViewById<TextView>(R.id.dateText).text=note?.time
        return view
    }
}