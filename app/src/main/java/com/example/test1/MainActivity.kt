package com.example.test1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation de FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Références vers les composants UI
        val email = findViewById<EditText>(R.id.usernameInput)
        val password = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val createAccountButton = findViewById<Button>(R.id.createAccountButton)
        val forgotPasswordText = findViewById<TextView>(R.id.forgotPasswordText)

        // -- BOUTON CONNEXION --
        loginButton.setOnClickListener {
            val user = email.text.toString()
            val pass = password.text.toString()

            // Vérifier que email et mot de passe ne sont pas vides
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Email et mot de passe requis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tentative de connexion avec email/mot de passe Firebase
            auth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show()

                        // Afficher dans Logcat l'UID de l'utilisateur connecté (pour debug)
                        Log.d("FirebaseUser", "Utilisateur connecté UID = ${auth.currentUser?.uid}")

                        // Redirection vers HomeActivity après connexion réussie
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)

                        // Finir MainActivity pour ne pas revenir en arrière sur la page de login
                        finish()
                    } else {
                        // Afficher l'erreur si la connexion échoue
                        Toast.makeText(this, "Erreur : ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // -- BOUTON CREATION DE COMPTE --
        createAccountButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        // -- MOT DE PASSE OUBLIE --
        forgotPasswordText.setOnClickListener {
            val userEmail = email.text.toString().trim()

            if (userEmail.isNotEmpty()) {
                // Envoyer un email de réinitialisation du mot de passe
                auth.sendPasswordResetEmail(userEmail)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Email de réinitialisation envoyé à $userEmail", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erreur : ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Veuillez entrer votre email", Toast.LENGTH_SHORT).show()
            }
        }

        // -- TESTER LA CONNEXION ANONYME (Optionnel, décommenter pour tester) --
        /*
        auth.signInAnonymously()
            .addOnSuccessListener {
                Toast.makeText(this, "Connexion anonyme réussie", Toast.LENGTH_SHORT).show()
                Log.d("FirebaseUser", "Connexion anonyme UID = ${auth.currentUser?.uid}")
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erreur connexion anonyme : ${it.message}", Toast.LENGTH_SHORT).show()
            }
        */
    }
}
