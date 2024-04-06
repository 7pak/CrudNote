package io.abood.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.abood.firebase.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityMain2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.titleTxt.text=intent.extras?.getString("title")
        binding.noteTxt.text=intent.extras?.getString("notes")



    }
}