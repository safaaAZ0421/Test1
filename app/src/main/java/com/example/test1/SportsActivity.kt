package com.example.test1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.adapter.ClubAdapter
import com.example.test1.model.Club
import com.example.test1.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SportsActivity : AppCompatActivity() {

    private lateinit var recyclerViewClubs: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewEmpty: TextView
    private lateinit var clubAdapter: ClubAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var isUserMember: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sports)

        initViews()
        setupRecyclerView()
        // Removed loadClubs() from here, it will be called by checkMembershipAndLoadClubs() in onResume
    }

    override fun onResume() {
        super.onResume()
        checkMembershipAndLoadClubs() // Call this here to ensure status is always fresh
    }

    private fun initViews() {
        recyclerViewClubs = findViewById(R.id.recyclerViewClubs)
        progressBar = findViewById(R.id.progressBar)
        textViewEmpty = findViewById(R.id.textViewEmpty)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    private fun setupRecyclerView() {
        clubAdapter = ClubAdapter(emptyList(), isUserMember) { club ->
            openClubDetails(club)
        }

        recyclerViewClubs.apply {
            layoutManager = LinearLayoutManager(this@SportsActivity)
            adapter = clubAdapter
        }
    }

    private fun checkMembershipAndLoadClubs() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.uid)
                .get(com.google.firebase.firestore.Source.SERVER)
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    isUserMember = user?.isMember ?: false
                    loadClubs()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erreur de chargement du statut d'adhésion: ${exception.message}", Toast.LENGTH_LONG).show()
                    isUserMember = false // Par défaut, non membre en cas d'erreur
                    loadClubs()
                }
        } else {
            isUserMember = false // Utilisateur non connecté, donc non membre
            loadClubs()
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
                    // Recréer l'adaptateur avec le statut d'adhésion mis à jour
                    clubAdapter = ClubAdapter(clubs, isUserMember) { club ->
                        openClubDetails(club)
                    }
                    recyclerViewClubs.adapter = clubAdapter
                }
            }
            .addOnFailureListener { exception ->
                showLoading(false)
                showEmptyState(true)
                Toast.makeText(this, "Erreur lors du chargement des clubs: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun openClubDetails(club: Club) {
        val intent = Intent(this, ClubDetailsActivity::class.java)
        intent.putExtra("club_id", club.id)
        intent.putExtra("is_member", isUserMember) // Passer le statut d'adhésion
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
