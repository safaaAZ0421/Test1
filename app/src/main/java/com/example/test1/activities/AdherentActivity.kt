/*package com.example.test1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.R
import com.example.test1.adapters.LivreAdapter
import com.example.test1.adapters.SalleAdapter
import com.example.test1.models.LivreItem
import com.example.test1.models.Salles
import com.google.firebase.firestore.FirebaseFirestore

class AdherentActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerViewLivres: RecyclerView
    private lateinit var recyclerViewSalles: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val livresList = mutableListOf<LivreItem>()
    private val sallesList = mutableListOf<Salles>()

    private lateinit var bibliothequeId: String  // Id de la bibliothèque passée en extra

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adherent)

        // Récupération de l'id de la bibliothèque
        bibliothequeId = intent.getStringExtra("bibliothequeId") ?: ""

        searchView = findViewById(R.id.searchView)
        recyclerViewLivres = findViewById(R.id.recyclerViewLivres)
        recyclerViewSalles = findViewById(R.id.recyclerViewSalles)

        val livreAdapter = LivreAdapter(livresList)
        recyclerViewLivres.layoutManager = LinearLayoutManager(this)
        recyclerViewLivres.adapter = livreAdapter

        val salleAdapter = SalleAdapter(sallesList, this)
        recyclerViewSalles.layoutManager = LinearLayoutManager(this)
        recyclerViewSalles.adapter = salleAdapter


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty() && bibliothequeId.isNotEmpty()) {
                    val intent = Intent(this@AdherentActivity, RechercheLivresActivity::class.java)
                    intent.putExtra("bibliothequeId", bibliothequeId)
                    intent.putExtra("query", query)
                    startActivity(intent)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Tu peux choisir de ne rien faire ici, ou idem lancer la recherche avec délai etc.
                return true
            }
        })

        if (bibliothequeId.isNotEmpty()) {
            chargerSalles(salleAdapter)
        } else {
            Log.e("Firebase", "Aucun bibliothequeId reçu !")
        }
    }

    private fun chargerSalles(salleAdapter: SalleAdapter) {
        // Accès aux sous-collection Salles dans la bibliothèque choisie
        db.collection("Bibliotheques")
            .document(bibliothequeId)
            .collection("Salles")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Firebase", "Firestore a retourné ${documents.size()} salles.")
                sallesList.clear()
                for (document in documents) {
                    val salle = document.toObject(Salles::class.java)
                    sallesList.add(salle)
                    Log.d("Firebase", "Salle ajoutée: ${salle.Identifiant}")
                }
                salleAdapter.notifyDataSetChanged()
                recyclerViewSalles.visibility = View.VISIBLE
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Erreur lors de la récupération des salles: ${e.message}")
            }
    }

    private fun rechercherLivre(query: String?, adapter: LivreAdapter) {
        if (query.isNullOrEmpty()) {
            recyclerViewLivres.visibility = View.GONE
            return
        }
        livresList.clear()

        // Accès aux sous-collection Livres dans la bibliothèque choisie
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
                Log.e("Firebase", "Erreur lors de la recherche de livres: ${e.message}")
            }
    }
}*/

/*package com.example.test1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.R
import com.example.test1.adapters.LivreAdapter
import com.example.test1.adapters.SalleAdapter
import com.example.test1.models.LivreItem
import com.example.test1.models.Salles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdherentActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerViewLivres: RecyclerView
    private lateinit var recyclerViewSalles: RecyclerView
    private lateinit var btnAnnulerAbonnement: Button
    private val db = FirebaseFirestore.getInstance()
    private val livresList = mutableListOf<LivreItem>()
    private val sallesList = mutableListOf<Salles>()

    private lateinit var bibliothequeId: String  // Id de la bibliothèque passée en extra

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adherent)

        // Récupération de l'id de la bibliothèque
        bibliothequeId = intent.getStringExtra("bibliothequeId") ?: ""

        searchView = findViewById(R.id.searchView)
        recyclerViewLivres = findViewById(R.id.recyclerViewLivres)
        recyclerViewSalles = findViewById(R.id.recyclerViewSalles)
        btnAnnulerAbonnement = findViewById(R.id.btnAnnulerAbonnement)

        val livreAdapter = LivreAdapter(livresList)
        recyclerViewLivres.layoutManager = LinearLayoutManager(this)
        recyclerViewLivres.adapter = livreAdapter

        val salleAdapter = SalleAdapter(sallesList, this)
        recyclerViewSalles.layoutManager = LinearLayoutManager(this)
        recyclerViewSalles.adapter = salleAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty() && bibliothequeId.isNotEmpty()) {
                    val intent = Intent(this@AdherentActivity, RechercheLivresActivity::class.java)
                    intent.putExtra("bibliothequeId", bibliothequeId)
                    intent.putExtra("query", query)
                    startActivity(intent)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        if (bibliothequeId.isNotEmpty()) {
            chargerSalles(salleAdapter)
        } else {
            Log.e("Firebase", "Aucun bibliothequeId reçu !")
        }

        // Gestion du clic sur le bouton annuler abonnement
        btnAnnulerAbonnement.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.collection("Bibliotheques")
                .document(bibliothequeId)
                .collection("Abonnements")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        for (doc in documents) {
                            db.collection("Bibliotheques")
                                .document(bibliothequeId)
                                .collection("Abonnements")
                                .document(doc.id)
                                .delete()
                        }
                        Toast.makeText(this, "Abonnement annulé", Toast.LENGTH_LONG).show()

                        // Ici on ne change rien, on reste sur la page

                    } else {
                        Toast.makeText(this, "Aucun abonnement trouvé", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erreur : ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("Firestore", "Erreur annulation : ", e)
                }
        }
    }

    private fun chargerSalles(salleAdapter: SalleAdapter) {
        db.collection("Bibliotheques")
            .document(bibliothequeId)
            .collection("Salles")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Firebase", "Firestore a retourné ${documents.size()} salles.")
                sallesList.clear()
                for (document in documents) {
                    val salle = document.toObject(Salles::class.java)
                    sallesList.add(salle)
                    Log.d("Firebase", "Salle ajoutée: ${salle.Identifiant}")
                }
                salleAdapter.notifyDataSetChanged()
                recyclerViewSalles.visibility = View.VISIBLE
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Erreur lors de la récupération des salles: ${e.message}")
            }
    }

    private fun rechercherLivre(query: String?, adapter: LivreAdapter) {
        if (query.isNullOrEmpty()) {
            recyclerViewLivres.visibility = View.GONE
            return
        }
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
                Log.e("Firebase", "Erreur lors de la recherche de livres: ${e.message}")
            }
    }
}*/



package com.example.test1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.R
import com.example.test1.adapters.LivreAdapter
import com.example.test1.adapters.SalleAdapter
import com.example.test1.models.LivreItem
import com.example.test1.models.Salles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdherentActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerViewLivres: RecyclerView
    private lateinit var recyclerViewSalles: RecyclerView
    private lateinit var btnAnnulerAbonnement: Button
    private val db = FirebaseFirestore.getInstance()
    private val livresList = mutableListOf<LivreItem>()
    private val sallesList = mutableListOf<Salles>()

    private lateinit var bibliothequeId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adherent)

        bibliothequeId = intent.getStringExtra("bibliothequeId") ?: ""

        searchView = findViewById(R.id.searchView)
        recyclerViewLivres = findViewById(R.id.recyclerViewLivres)
        recyclerViewSalles = findViewById(R.id.recyclerViewSalles)
        btnAnnulerAbonnement = findViewById(R.id.btnAnnulerAbonnement)

        val livreAdapter = LivreAdapter(livresList)
        recyclerViewLivres.layoutManager = LinearLayoutManager(this)
        recyclerViewLivres.adapter = livreAdapter

        // Passage de bibliothequeId dans le constructeur de SalleAdapter
        val salleAdapter = SalleAdapter(sallesList, this, bibliothequeId)
        recyclerViewSalles.layoutManager = LinearLayoutManager(this)
        recyclerViewSalles.adapter = salleAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty() && bibliothequeId.isNotEmpty()) {
                    val intent = Intent(this@AdherentActivity, RechercheLivresActivity::class.java)
                    intent.putExtra("bibliothequeId", bibliothequeId)
                    intent.putExtra("query", query)
                    startActivity(intent)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        if (bibliothequeId.isNotEmpty()) {
            chargerSalles(salleAdapter)
        } else {
            Log.e("Firebase", "Aucun bibliothequeId reçu !")
        }

        btnAnnulerAbonnement.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.collection("Bibliotheques")
                .document(bibliothequeId)
                .collection("Abonnements")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        for (doc in documents) {
                            db.collection("Bibliotheques")
                                .document(bibliothequeId)
                                .collection("Abonnements")
                                .document(doc.id)
                                .delete()
                        }
                        Toast.makeText(this, "Abonnement annulé", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Aucun abonnement trouvé", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erreur : ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("Firestore", "Erreur annulation : ", e)
                }
        }
    }

    private fun chargerSalles(salleAdapter: SalleAdapter) {
        db.collection("Bibliotheques")
            .document(bibliothequeId)
            .collection("Salles")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Firebase", "Firestore a retourné ${documents.size()} salles.")
                sallesList.clear()
                for (document in documents) {
                    val salle = document.toObject(Salles::class.java)
                    sallesList.add(salle)
                    Log.d("Firebase", "Salle ajoutée: ${salle.salleId}")
                }
                salleAdapter.notifyDataSetChanged()
                recyclerViewSalles.visibility = View.VISIBLE
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Erreur lors de la récupération des salles: ${e.message}")
            }
    }

    private fun rechercherLivre(query: String?, adapter: LivreAdapter) {
        if (query.isNullOrEmpty()) {
            recyclerViewLivres.visibility = View.GONE
            return
        }
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
                Log.e("Firebase", "Erreur lors de la recherche de livres: ${e.message}")
            }
    }
}




