package com.example.test1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.content.Intent
import android.util.Log

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val sexeSpinner = findViewById<Spinner>(R.id.sexeSpinner)
        val sexes = arrayOf("Homme", "Femme")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sexes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sexeSpinner.adapter = adapter

        val nomInput = findViewById<EditText>(R.id.nomInput)
        val prenomInput = findViewById<EditText>(R.id.prenomInput)
        val nomutilisateurInput = findViewById<EditText>(R.id.nomutilisateurInput)
        val telephoneInput = findViewById<EditText>(R.id.telephoneInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val repeatPasswordInput = findViewById<EditText>(R.id.repeatPasswordInput)
        val signupButton = findViewById<Button>(R.id.signupButton)

        signupButton.setOnClickListener {
            val nom = nomInput.text.toString()
            val prenom = prenomInput.text.toString()
            val username = nomutilisateurInput.text.toString()
            val sexe = sexeSpinner.selectedItem.toString()
            val telephone = telephoneInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            val repeatPassword = repeatPasswordInput.text.toString()

            if (password == repeatPassword && email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        val userId = result.user?.uid ?: return@addOnSuccessListener

                        val userMap = hashMapOf(
                            "name" to nom,
                            "surname" to prenom,
                            "username" to username,
                            "sexe" to sexe,
                            "telephone" to telephone,
                            "email" to email
                        )

                        Log.d("FIREBASE_DEBUG", "Données prêtes pour Firestore:\n${userMap.entries.joinToString("\n") { "${it.key}=${it.value}" }}")

                        db.collection("users").document(userId).set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Compte créé avec succès", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Erreur Firestore: ${e.message}", Toast.LENGTH_LONG).show()
                                Log.e("SignupError", "Erreur Firestore", e)
                            }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erreur Auth: ${e.message}", Toast.LENGTH_LONG).show()
                        Log.e("SignupError", "Erreur Auth", e)
                    }
            } else {
                Toast.makeText(this, "Veuillez remplir les champs correctement", Toast.LENGTH_SHORT).show()
            }
            //go to page login
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
}
