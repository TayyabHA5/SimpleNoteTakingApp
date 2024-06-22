package com.example.simplenotetakingapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MyAdapter(
    val context: Context,
    val arr: ArrayList<Model>,
    private var db: FirebaseFirestore
) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.textView1)
        val btnOpen = view.findViewById<Button>(R.id.buttonOpen)
        val btnDelete = view.findViewById<Button>(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noteItem = arr[position]
        holder.textView.text = noteItem.text

        holder.btnOpen.setOnClickListener() {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("ITEM", noteItem.text)
            context.startActivity(intent)
        }
        holder.btnDelete.setOnClickListener() {
            db.collection("notes").document(noteItem.Id)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Item Deleted ${noteItem.text}", Toast.LENGTH_SHORT)
                        .show()
                    arr.removeAt(position)
                    notifyDataSetChanged()
                }
        }
    }

}
