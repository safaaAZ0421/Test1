/*package com.example.test1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.test1.R

class BibliothequeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bibliotheque_detail)

        val nom = intent.getStringExtra("nom")
        val adresse = intent.getStringExtra("adresse")
        val horaire = intent.getStringExtra("horaire")
        val imageUrl = intent.getStringExtra("imageUrl")

        val nomTextView = findViewById<TextView>(R.id.nomTextView)
        val adresseTextView = findViewById<TextView>(R.id.adresseTextView)
        val horaireTextView = findViewById<TextView>(R.id.horaireTextView)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val btnInscrire = findViewById<TextView>(R.id.btnInscrire)

        // Remplir les informations
        nomTextView.text = nom
        adresseTextView.text = adresse
        horaireTextView.text = horaire

        // Afficher l'image avec Glide
        Glide.with(this).load(imageUrl).into(imageView)

        // Action du bouton "S'inscrire"
        btnInscrire.setOnClickListener {
                val intent = Intent(this, ForfaitsActivity::class.java)
                startActivity(intent)
        }
    }
}*/
/*package com.example.test1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.test1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BibliothequeDetailActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bibliotheque_detail)

        val nom = intent.getStringExtra("nom")
        val adresse = intent.getStringExtra("adresse")
        val horaire = intent.getStringExtra("horaire")
        val imageUrl = intent.getStringExtra("imageUrl")
        val bibliothequeId = intent.getStringExtra("bibliothequeId")

        val nomTextView = findViewById<TextView>(R.id.nomTextView)
        val adresseTextView = findViewById<TextView>(R.id.adresseTextView)
        val horaireTextView = findViewById<TextView>(R.id.horaireTextView)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val btnInscrire = findViewById<TextView>(R.id.btnInscrire)

        // Nouveau bouton/texte pour les adhérents
        val btnVoirPlus = findViewById<TextView>(R.id.btnVoirPlus)

        // Remplir les informations
        nomTextView.text = nom
        adresseTextView.text = adresse
        horaireTextView.text = horaire

        // Afficher l'image avec Glide
        Glide.with(this).load(imageUrl).into(imageView)

        // Par défaut, on cache le bouton "Voir plus"
        btnVoirPlus.visibility = TextView.GONE

        val userId = auth.currentUser?.uid
        if (userId == null) {
            // Pas connecté : on affiche juste le bouton s'inscrire, pas le "voir plus"
            btnInscrire.visibility = TextView.VISIBLE
            btnVoirPlus.visibility = TextView.GONE
        } else if (bibliothequeId != null) {
            // Vérifier si l'utilisateur est déjà abonné à cette bibliothèque
            db.collection("Bibliotheques").document(bibliothequeId)
                .collection("Abonnements")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        // Utilisateur déjà abonné : cacher s'inscrire, afficher "voir plus"
                        btnInscrire.visibility = TextView.GONE
                        btnVoirPlus.visibility = TextView.VISIBLE
                    } else {
                        // Pas abonné : afficher s'inscrire, cacher voir plus
                        btnInscrire.visibility = TextView.VISIBLE
                        btnVoirPlus.visibility = TextView.GONE
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Erreur vérification abonnement : ", e)
                    Toast.makeText(this, "Erreur de connexion, réessayez plus tard", Toast.LENGTH_SHORT).show()
                    // Par sécurité afficher bouton s'inscrire
                    btnInscrire.visibility = TextView.VISIBLE
                    btnVoirPlus.visibility = TextView.GONE
                }
        } else {
            // Pas de bibliothequeId reçu, afficher bouton s'inscrire par défaut
            btnInscrire.visibility = TextView.VISIBLE
            btnVoirPlus.visibility = TextView.GONE
        }

        // Action du bouton "S'inscrire"
        btnInscrire.setOnClickListener {
            val intent = Intent(this, ForfaitsActivity::class.java)
            intent.putExtra("bibliothequeId", bibliothequeId)
            startActivity(intent)
        }

        // Action du bouton "Voir plus" pour aller à AdherentActivity
        btnVoirPlus.setOnClickListener {
            val intent = Intent(this, AdherentActivity::class.java)
            intent.putExtra("bibliothequeId", bibliothequeId)
            startActivity(intent)
        }
    }
}*/


package com.example.test1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.test1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class BibliothequeDetailActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bibliotheque_detail)

        val nom = intent.getStringExtra("nom")
        val adresse = intent.getStringExtra("adresse")
        val horaire = intent.getStringExtra("horaire")
        val imageUrl = intent.getStringExtra("imageUrl")
        val bibliothequeId = intent.getStringExtra("bibliothequeId")

        val nomTextView = findViewById<TextView>(R.id.nomTextView)
        val adresseTextView = findViewById<TextView>(R.id.adresseTextView)
        val horaireTextView = findViewById<TextView>(R.id.horaireTextView)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val btnAcceder = findViewById<TextView>(R.id.btnInscrire) // renommé en bouton "Accéder"

        // Remplir les informations
        nomTextView.text = nom
        adresseTextView.text = adresse
        horaireTextView.text = horaire

        // Afficher l'image avec Glide
        Glide.with(this).load(imageUrl).into(imageView)

        // Action du bouton "Accéder"
        btnAcceder.setOnClickListener {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                Toast.makeText(this, "Veuillez vous connecter d'abord.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (bibliothequeId == null) {
                Toast.makeText(this, "Bibliothèque non spécifiée.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Vérifie s'il y a un abonnement actif pour cet utilisateur
            db.collection("Bibliotheques")
                .document(bibliothequeId)
                .collection("Abonnements")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val abonnement = documents.first()
                        val finAbonnementTimestamp = abonnement.getTimestamp("finAbonnement")
                        val finAbonnementDate = finAbonnementTimestamp?.toDate()

                        if (finAbonnementDate != null && finAbonnementDate.after(Date())) {
                            // L'abonnement est encore valide → accéder aux salles
                            val intent = Intent(this, AdherentActivity::class.java)
                            intent.putExtra("bibliothequeId", bibliothequeId)
                            startActivity(intent)
                        } else {
                            // Abonnement expiré → redirection vers les forfaits
                            Toast.makeText(this, "Votre abonnement a expiré.", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, ForfaitsActivity::class.java)
                            intent.putExtra("bibliothequeId", bibliothequeId)
                            startActivity(intent)
                        }
                    } else {
                        // Aucun abonnement trouvé → redirection vers les forfaits
                        val intent = Intent(this, ForfaitsActivity::class.java)
                        intent.putExtra("bibliothequeId", bibliothequeId)
                        startActivity(intent)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erreur lors de la vérification d'abonnement.", Toast.LENGTH_SHORT).show()
                }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // L'utilisateur a autorisé les notifications ✅
            } else {
                Toast.makeText(this, "Notifications refusées, vous ne recevrez pas de rappels.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}


