package com.example.test1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.adapter.ClubAdapter
import com.example.test1.model.Club
import com.google.firebase.firestore.FirebaseFirestore

class SportsActivity : AppCompatActivity() {

    private lateinit var recyclerViewClubs: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewEmpty: TextView
    private lateinit var clubAdapter: ClubAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sports)

        initViews()
        setupRecyclerView()
        loadClubs()
    }

    private fun initViews() {
        recyclerViewClubs = findViewById(R.id.recyclerViewClubs)
        progressBar = findViewById(R.id.progressBar)
        textViewEmpty = findViewById(R.id.textViewEmpty)
        firestore = FirebaseFirestore.getInstance()
    }

    private fun setupRecyclerView() {
        clubAdapter = ClubAdapter(emptyList()) { club ->
            openClubDetails(club)
        }

        recyclerViewClubs.apply {
            layoutManager = LinearLayoutManager(this@SportsActivity)
            adapter = clubAdapter
        }
    }

    private fun loadClubs() {
        showLoading(true)

        firestore.collection("clubs")
            .get()
            .addOnSuccessListener { documents ->
                val clubs = mutableListOf<Club>()

                for (document in documents) {
                    val club = document.toObject(Club::class.java)
                    club.id = document.id
                    clubs.add(club)
                }

                showLoading(false)

                if (clubs.isEmpty()) {
                    showEmptyState(true)
                } else {
                    showEmptyState(false)
                    clubAdapter.updateClubs(clubs)
                }
            }
            .addOnFailureListener { exception ->
                showLoading(false)
                showEmptyState(true)
                Log.e("SportsActivity", "Erreur lors du chargement des clubs: ", exception)
                Toast.makeText(this, "Erreur lors du chargement des clubs: ${exception.message}", Toast.LENGTH_LONG).show()
            }

            /*.addOnFailureListener { exception ->
                showLoading(false)
                showEmptyState(true)
                // Vous pouvez ajouter ici une gestion d'erreur plus sophistiquée
                // comme afficher un Toast ou un Snackbar
            }*/
    }

    private fun openClubDetails(club: Club) {
        val intent = Intent(this, ClubDetailsActivity::class.java)
        intent.putExtra("club_id", club.id)
        startActivity(intent)
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        recyclerViewClubs.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showEmptyState(show: Boolean) {
        textViewEmpty.visibility = if (show) View.VISIBLE else View.GONE
        recyclerViewClubs.visibility = if (show) View.GONE else View.VISIBLE
    }
}

