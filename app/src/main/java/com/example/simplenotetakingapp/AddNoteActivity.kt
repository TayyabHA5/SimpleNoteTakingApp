package com.example.simplenotetakingapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.simplenotetakingapp.databinding.ActivityAddNoteBinding
import com.example.simplenotetakingapp.databinding.ActivityMainBinding

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddNote.setOnClickListener(){
            val etAddNote = binding.editTextText.text.toString().trim()
            if (!etAddNote.isEmpty()){
                val intent = Intent()
                intent.putExtra("NEW_NOTE",etAddNote)
                setResult(RESULT_OK,intent)
                finish()
            }
        }

    }
}