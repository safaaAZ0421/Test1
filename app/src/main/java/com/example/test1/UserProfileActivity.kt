package com.example.test1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class UserProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Masquer la barre de navigation
        supportActionBar?.hide()

        setContentView(R.layout.activity_user_profile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Références UI
        val btnAfficher = findViewById<Button>(R.id.btnAfficherInfos)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val layoutInfos = findViewById<LinearLayout>(R.id.layoutInfos)

        val nomInput = findViewById<EditText>(R.id.nomInput)
        val prenomInput = findViewById<EditText>(R.id.prenomInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val telephoneInput = findViewById<EditText>(R.id.telephoneInput)
        val sexeSpinner = findViewById<Spinner>(R.id.sexeSpinner)
        val newPasswordInput = findViewById<EditText>(R.id.newPasswordInput)
        val btnChangePassword = findViewById<Button>(R.id.btnChangePassword)

        val btnPrestataire = findViewById<Button>(R.id.btnDevenirPrestataire)
        val btnLogout = findViewById<Button>(R.id.btnDeconnexion)

        // Initialiser le spinner de sexe
        val sexes = arrayOf("Homme", "Femme")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sexes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sexeSpinner.adapter = adapter

        val userId = auth.currentUser?.uid ?: return

        //  Afficher infos utilisateur
        btnAfficher.setOnClickListener {
            layoutInfos.visibility = View.VISIBLE

            db.collection("users").document(userId).get()
                .addOnSuccessListener { doc ->
                    nomInput.setText(doc.getString("name") ?: "")
                    prenomInput.setText(doc.getString("surname") ?: "")
                    emailInput.setText(doc.getString("email") ?: "")
                    telephoneInput.setText(doc.getString("telephone") ?: "")
                    val sexe = doc.getString("sexe") ?: "Homme"
                    sexeSpinner.setSelection(sexes.indexOf(sexe))
                }
        }

        //  Enregistrer modifications
        btnSave.setOnClickListener {
            val updates = mapOf(
                "name" to nomInput.text.toString(),
                "surname" to prenomInput.text.toString(),
                "email" to emailInput.text.toString(),
                "telephone" to telephoneInput.text.toString(),
                "sexe" to sexeSpinner.selectedItem.toString()
            )

            db.collection("users").document(userId).update(updates)
                .addOnSuccessListener {
                    Toast.makeText(this, "Données mises à jour", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erreur : ${it.message}", Toast.LENGTH_LONG).show()
                }
        }

        //  Modifier mot de passe
        btnChangePassword.setOnClickListener {
            val newPassword = newPasswordInput.text.toString()

            if (newPassword.length < 6) {
                Toast.makeText(this, "Le mot de passe doit contenir au moins 6 caractères", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.currentUser?.updatePassword(newPassword)
                ?.addOnSuccessListener {
                    Toast.makeText(this, "Mot de passe modifié avec succès", Toast.LENGTH_SHORT).show()
                    newPasswordInput.text.clear()
                }
                ?.addOnFailureListener {
                    Toast.makeText(this, "Erreur : ${it.message}", Toast.LENGTH_LONG).show()
                }
        }

        //Changer de langue
        val btnChangeLanguage = findViewById<Button>(R.id.btnChangeLanguage)

        btnChangeLanguage.setOnClickListener {
            val currentLocale = resources.configuration.locales[0].language
            val newLocale = if (currentLocale == "fr") "en" else "fr"

            val locale = Locale(newLocale)
            Locale.setDefault(locale)

            val config = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)

            // Recharger l'activité pour appliquer la nouvelle langue
            val intent = intent
            finish()
            startActivity(intent)
        }


        //  Redirection vers activité pour devenir prestataire
        btnPrestataire.setOnClickListener {
            startActivity(Intent(this, BecomeProviderActivity::class.java))
        }

        // Déconnexion
        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
