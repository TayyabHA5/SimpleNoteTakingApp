package com.example.simplenotetakingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplenotetakingapp.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val REQUEST_CODE_ADD_NOTE: Int = 1
    private lateinit var adapter: MyAdapter
    private var listItem: ArrayList<Model> = ArrayList()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MyAdapter(this, listItem, db)

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        binding.btnAddNote.setOnClickListener {
            val intent = Intent(this@MainActivity, AddNoteActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_NOTE)
        }
        loadNotesFromFirestore()
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            val newNote = data?.getStringExtra("NEW_NOTE")
            if (newNote != null) {
                saveNoteToFireStore(newNote)
            }
        }
    }

    private fun loadNotesFromFirestore() {
        db.collection("notes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val note = document.getString("text")
                    if (note != null) {
                        listItem.add(Model(document.id, note))
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Toast.makeText(this, "Error loading notes: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveNoteToFireStore(newNote: String) {
        val noteData = hashMapOf("text" to newNote)
        db.collection("notes").add(noteData)
            .addOnSuccessListener { docRef ->
                Toast.makeText(this, "Note added successfully", Toast.LENGTH_SHORT).show()
                listItem.add(Model(docRef.id, newNote))
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add note", Toast.LENGTH_SHORT).show()
            }
    }
}
