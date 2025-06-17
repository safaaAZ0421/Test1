package com.example.test1.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.R
import com.example.test1.adapters.LivreAdapter
import com.example.test1.models.LivreItem
import com.google.firebase.firestore.FirebaseFirestore

class RechercheLivresActivity : AppCompatActivity() {

    private lateinit var recyclerViewLivres: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val livresList = mutableListOf<LivreItem>()

    private lateinit var bibliothequeId: String
    private lateinit var query: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recherche_livres)

        recyclerViewLivres = findViewById(R.id.recyclerViewLivres)
        val livreAdapter = LivreAdapter(livresList)
        recyclerViewLivres.layoutManager = LinearLayoutManager(this)
        recyclerViewLivres.adapter = livreAdapter

        // Récupération des extras
        bibliothequeId = intent.getStringExtra("bibliothequeId") ?: ""
        query = intent.getStringExtra("query") ?: ""

        if (query.isNotEmpty() && bibliothequeId.isNotEmpty()) {
            rechercherLivres(query, livreAdapter)
        } else {
            Log.e("RechercheLivres", "query ou bibliothequeId manquant")
        }
    }

    private fun rechercherLivres(query: String, adapter: LivreAdapter) {
        livresList.clear()

        db.collection("Bibliotheques")
            .document(bibliothequeId)
            .collection("Livres")
            .whereEqualTo("Titre", query)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val livre = document.toObject(LivreItem::class.java)
                    livresList.add(livre)
                }
                adapter.notifyDataSetChanged()
                recyclerViewLivres.visibility = if (livresList.isEmpty()) View.GONE else View.VISIBLE
            }
            .addOnFailureListener { e ->
                Log.e("RechercheLivres", "Erreur lors de la recherche: ${e.message}")
            }
    }
}
