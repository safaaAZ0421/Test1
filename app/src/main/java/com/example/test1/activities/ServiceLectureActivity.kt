package com.example.test1.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.example.test1.models.BibliothequeItem
import com.example.test1.adapters.BibliothequeAdapter
import com.example.test1.R
import com.google.firebase.auth.FirebaseAuth

class ServiceLectureActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var bibliothequeAdapter: BibliothequeAdapter
    private val bibliothequesList = mutableListOf<BibliothequeItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_lecture)

        db = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        bibliothequeAdapter = BibliothequeAdapter(bibliothequesList)
        recyclerView.adapter = bibliothequeAdapter

        recupererBibliotheques()
    }

    private fun recupererBibliotheques() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            db.collection("Bibliotheques")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val bibliotheque = document.toObject(BibliothequeItem::class.java).copy(id = document.id)
                        bibliothequesList.add(bibliotheque)
                        Log.d("Firestore", "Ajout: ${bibliotheque.nom}")

                    }
                    bibliothequeAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Veuillez vous connecter pour voir les bibliothèques.", Toast.LENGTH_SHORT).show()
        }
    }
}