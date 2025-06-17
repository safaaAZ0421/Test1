package com.example.test1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MembershipActivity : AppCompatActivity() {

    private lateinit var buttonSubscribe: MaterialButton
    private lateinit var progressBarPayment: ProgressBar

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var btnSubscribe: Button
    private lateinit var userId: String
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_membership)

        initViews()
        setupAuth()
        setupClickListeners()
        //setupToolbar()
    }

    private fun initViews() {
        buttonSubscribe = findViewById(R.id.buttonSubscribe)
        progressBarPayment = findViewById(R.id.progressBarPayment)
    }

    private fun setupAuth() {
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    /*private fun setupToolbar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Adhésion Premium"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }*/

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
        // Dans une vraie application, vous intégreriez ici un système de paiement comme Stripe, PayPal, etc.
        simulatePaymentProcess()
    }

    private fun simulatePaymentProcess() {
        // Simulation d'un délai de traitement de paiement
        buttonSubscribe.postDelayed({
            // Simuler un paiement réussi (dans 90% des cas pour la démo)
            val paymentSuccess = (1..10).random() <= 9

            if (paymentSuccess) {
                processSuccessfulPayment()
            } else {
                processFailedPayment()
            }
        }, 2000) // Délai de 2 secondes pour simuler le traitement
    }

    private fun processSuccessfulPayment() {
        val currentUser = auth.currentUser ?: return

        // Mettre à jour le statut d'adhésion dans Firestore
        firestore.collection("users").document(currentUser.uid)
            .update("isMember", true)
            .addOnSuccessListener {
                showLoading(false)
                showSuccessMessage()

                // Retourner à l'activité précédente avec un résultat
                setResult(RESULT_OK)
                finish()
            }
            .addOnFailureListener { exception ->
                showLoading(false)
                Toast.makeText(
                    this,
                    "Erreur lors de la mise à jour du statut: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun processFailedPayment() {
        showLoading(false)
        Toast.makeText(
            this,
            "Échec du paiement. Veuillez réessayer ou vérifier vos informations de paiement.",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showSuccessMessage() {
        Toast.makeText(
            this,
            "Félicitations ! Vous êtes maintenant membre . Profitez de tous les avantages !",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoading(show: Boolean) {
        progressBarPayment.visibility = if (show) View.VISIBLE else View.GONE
        buttonSubscribe.isEnabled = !show

        if (show) {
            buttonSubscribe.text = "Traitement en cours..."
        } else {
            buttonSubscribe.text = "Devenir Membre "
        }
    }
}

