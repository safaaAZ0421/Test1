/*package com.example.test1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test1.R

class FormulaireAbonnementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulaire_abonnement)

        val forfaitTextView = findViewById<TextView>(R.id.forfaitTextView)
        val prixTextView = findViewById<TextView>(R.id.prixTextView)
        val nomEditText = findViewById<EditText>(R.id.nomEditText)
        val prenomEditText = findViewById<EditText>(R.id.prenomEditText)
        val dateNaissanceEditText = findViewById<EditText>(R.id.dateNaissanceEditText)
        val carteBancaireEditText = findViewById<EditText>(R.id.carteBancaireEditText)
        val codeVerificationEditText = findViewById<EditText>(R.id.codeVerificationEditText)
        val dateExpirationEditText = findViewById<EditText>(R.id.dateExpirationEditText)
        val btnPayer = findViewById<Button>(R.id.btnPayer)

        // Récupération du forfait, du prix et de l'id de la bibliothèque
        val forfait = intent.getStringExtra("forfait")
        val prix = intent.getStringExtra("prix")
        val bibliothequeId = intent.getStringExtra("bibliothequeId") // 👈 à récupérer

        forfaitTextView.text = "Forfait choisi: $forfait"
        prixTextView.text = "Prix: $prix"

        btnPayer.setOnClickListener {
            if (
                nomEditText.text.isNotEmpty() &&
                prenomEditText.text.isNotEmpty() &&
                dateNaissanceEditText.text.isNotEmpty() &&
                carteBancaireEditText.text.isNotEmpty() &&
                codeVerificationEditText.text.isNotEmpty() &&
                dateExpirationEditText.text.isNotEmpty()
            ) {
                Toast.makeText(this, "Paiement en cours...", Toast.LENGTH_SHORT).show()

                btnPayer.postDelayed({
                    Toast.makeText(this, "Paiement réussi! Vous êtes maintenant adhérent.", Toast.LENGTH_LONG).show()

                    // Log pour debug
                    Log.d("Navigation", "Redirection vers AdherentActivity avec bibliothequeId=$bibliothequeId")

                    // 🔁 Redirection vers AdherentActivity avec l’ID de la bibliothèque
                    val intent = Intent(this, AdherentActivity::class.java)
                    intent.putExtra("bibliothequeId", bibliothequeId) // ✅ AJOUT ESSENTIEL
                    startActivity(intent)

                    finish()
                }, 5000)
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}*/
package com.example.test1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test1.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import java.text.SimpleDateFormat
import java.util.*
//import com.example.test1.utils.NotificationScheduler

class FormulaireAbonnementActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulaire_abonnement)

        val forfaitTextView = findViewById<TextView>(R.id.forfaitTextView)
        val prixTextView = findViewById<TextView>(R.id.prixTextView)
        val nomEditText = findViewById<EditText>(R.id.nomEditText)
        val prenomEditText = findViewById<EditText>(R.id.prenomEditText)
        val dateNaissanceEditText = findViewById<EditText>(R.id.dateNaissanceEditText)
        val carteBancaireEditText = findViewById<EditText>(R.id.carteBancaireEditText)
        val codeVerificationEditText = findViewById<EditText>(R.id.codeVerificationEditText)
        val dateExpirationEditText = findViewById<EditText>(R.id.dateExpirationEditText)
        val btnPayer = findViewById<Button>(R.id.btnPayer)

        val forfait = intent.getStringExtra("forfait")
        val prix = intent.getStringExtra("prix")
        val bibliothequeId = intent.getStringExtra("bibliothequeId")

        forfaitTextView.text = "Forfait choisi: $forfait"
        prixTextView.text = "Prix: $prix"

        btnPayer.setOnClickListener {
            val nom = nomEditText.text.toString().trim()
            val prenom = prenomEditText.text.toString().trim()
            val dateNaissance = dateNaissanceEditText.text.toString().trim()
            val carteBancaire = carteBancaireEditText.text.toString().trim()
            val codeVerification = codeVerificationEditText.text.toString().trim()
            val dateExpiration = dateExpirationEditText.text.toString().trim()

            var isValid = true

            if (nom.isEmpty()) {
                nomEditText.error = "Nom obligatoire"
                isValid = false
            }

            if (prenom.isEmpty()) {
                prenomEditText.error = "Prénom obligatoire"
                isValid = false
            }

            if (!isDateNaissanceValide(dateNaissance)) {
                dateNaissanceEditText.error = "Date de naissance invalide"
                isValid = false
            }

            if (!isCarteBancaireValide(carteBancaire)) {
                carteBancaireEditText.error = "Numéro de carte invalide (16 chiffres)"
                isValid = false
            }

            if (codeVerification.length !in 3..4 || !codeVerification.all { it.isDigit() }) {
                codeVerificationEditText.error = "Code de vérification invalide"
                isValid = false
            }

            if (!isDateExpirationValide(dateExpiration)) {
                dateExpirationEditText.error = "Date d'expiration invalide ou expirée"
                isValid = false
            }

            if (isValid) {
                Toast.makeText(this, "Paiement en cours...", Toast.LENGTH_SHORT).show()

                val userId = auth.currentUser?.uid
                if (userId == null) {
                    Toast.makeText(this, "Utilisateur non authentifié !", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                // 🔥 Calcul de la date de fin d’abonnement (1 mois)
                val calendar = Calendar.getInstance()
                val dateDebut = calendar.time
                calendar.add(Calendar.MONTH, 1)
                val dateFin = calendar.time

                val abonnementData = hashMapOf(
                    "userId" to userId,
                    "nom" to nom,
                    "prenom" to prenom,
                    "dateNaissance" to dateNaissance,
                    "carteBancaire" to carteBancaire,
                    "codeVerification" to codeVerification,
                    "dateExpiration" to dateExpiration,
                    "forfait" to forfait,
                    "prix" to prix,
                    "timestamp" to FieldValue.serverTimestamp(),
                    "dateFin" to dateFin // 🔥 Champ ajouté ici
                )

                db.collection("Bibliotheques").document(bibliothequeId ?: "default")
                    .collection("Abonnements")
                    .add(abonnementData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Paiement réussi! Vous êtes maintenant adhérent.", Toast.LENGTH_LONG).show()
                        val finAbonnementDate = Calendar.getInstance().apply {
                            add(Calendar.MONTH, 1)
                        }.time

                        abonnementData["finAbonnement"] = finAbonnementDate
/*
// Planifier la notification
                        NotificationScheduler.createNotificationChannel(this)
                        NotificationScheduler.scheduleNotification(this, finAbonnementDate)

*/
                        Log.d("Navigation", "Redirection vers AdherentActivity avec bibliothequeId=$bibliothequeId")

                        val intent = Intent(this, AdherentActivity::class.java)
                        intent.putExtra("bibliothequeId", bibliothequeId)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erreur lors de l'abonnement : ${e.message}", Toast.LENGTH_LONG).show()
                        Log.e("Firestore", "Erreur abonnement: ", e)
                    }
            } else {
                Toast.makeText(this, "Veuillez corriger les erreurs dans le formulaire.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isDateNaissanceValide(dateStr: String): Boolean {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        sdf.isLenient = false
        return try {
            val date = sdf.parse(dateStr)
            date != null
        } catch (e: Exception) {
            false
        }
    }

    private fun isCarteBancaireValide(cardNumber: String): Boolean {
        return cardNumber.length == 16 && cardNumber.all { it.isDigit() }
    }

    private fun isDateExpirationValide(expDate: String): Boolean {
        val parts = expDate.split("/")
        if (parts.size != 2) return false
        val month = parts[0].toIntOrNull() ?: return false
        val year = parts[1].toIntOrNull() ?: return false
        if (month !in 1..12) return false

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR) % 100
        val currentMonth = calendar.get(Calendar.MONTH) + 1

        return (year > currentYear) || (year == currentYear && month >= currentMonth)
    }
}


