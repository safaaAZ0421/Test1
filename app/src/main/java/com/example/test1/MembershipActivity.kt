package com.example.test1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MembershipActivity : AppCompatActivity() {

    private lateinit var buttonSubscribe: MaterialButton
    private lateinit var progressBarPayment: ProgressBar
    private lateinit var textViewMembershipPrice: TextView // Nouveau TextView pour le prix

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var clubPrice: Double = 0.0 // Variable pour stocker le prix du club

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_membership)

        clubPrice = intent.getDoubleExtra("club_price", 0.0) // Récupérer le prix du club

        initViews()
        setupAuth()
        setupClickListeners()
        setupToolbar()
        displayClubPrice()
    }

    private fun initViews() {
        buttonSubscribe = findViewById(R.id.buttonSubscribe)
        progressBarPayment = findViewById(R.id.progressBarPayment)
        textViewMembershipPrice = findViewById(R.id.textViewMembershipPrice) // Initialiser le TextView
    }

    private fun setupAuth() {
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Adhésion Premium"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun displayClubPrice() {
        if (clubPrice > 0) {
            textViewMembershipPrice.text = "${clubPrice.toInt()}€" // Afficher le prix du club
        } else {
            textViewMembershipPrice.text = "Prix sur demande"
        }
    }

    private fun setupClickListeners() {
        buttonSubscribe.setOnClickListener {
            handleSubscription()
        }
    }

    private fun handleSubscription() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Veuillez vous connecter pour devenir membre", Toast.LENGTH_LONG).show()
            return
        }

        showLoading(true)

        // Simuler un processus de paiement
        simulatePaymentProcess()
    }

    private fun simulatePaymentProcess() {
        // Simulation d'un délai de traitement de paiement
        buttonSubscribe.postDelayed({
            processSuccessfulPayment()
        }, 2000) // Délai de 2 secondes pour simuler le traitement
    }

    private fun processSuccessfulPayment() {
        val currentUser = auth.currentUser ?: return

        Log.d("MembershipActivity", "Tentative de mise à jour de isMember pour l'utilisateur: ${currentUser.uid}")

        firestore.collection("users").document(currentUser.uid)
            .update("isMember", true)
            .addOnSuccessListener {
                Log.d("MembershipActivity", "Statut isMember mis à jour avec succès pour: ${currentUser.uid}")
                showLoading(false)
                showSuccessMessage()
                setResult(RESULT_OK)
                finish()
            }
            .addOnFailureListener { exception ->
                Log.e("MembershipActivity", "Erreur lors de la mise à jour de isMember: ${exception.message}", exception)
                showLoading(false)
                Toast.makeText(
                    this,
                    "Erreur lors de la mise à jour du statut: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }


    private fun showSuccessMessage() {
        Toast.makeText(
            this,
            "Félicitations ! Vous êtes maintenant membre premium.",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun processFailedPayment() {
        // Cette fonction n'est plus utilisée car le paiement est toujours simulé comme réussi
    }

    private fun showLoading(show: Boolean) {
        progressBarPayment.visibility = if (show) View.VISIBLE else View.GONE
        buttonSubscribe.isEnabled = !show

        if (show) {
            buttonSubscribe.text = "Traitement en cours..."
        } else {
            buttonSubscribe.text = "Devenir Membre Premium"
        }
    }
}