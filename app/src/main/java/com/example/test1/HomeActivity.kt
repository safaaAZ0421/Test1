package com.example.test1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Vérifie si l'utilisateur est connecté
        if (auth.currentUser == null) {
            Toast.makeText(this, "Session expirée, retour à la connexion", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        val profileIcon = findViewById<ImageView>(R.id.profileIcon)
        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val serviceGrid = findViewById<GridLayout>(R.id.serviceGrid)
        val userId = auth.currentUser?.uid ?: return

        // Récupération du nom et du sexe pour personnalisation
        FirebaseFirestore.getInstance().collection("users").document(userId)
            .get()
            .addOnSuccessListener { doc ->
                val prenom = doc.getString("surname") ?: ""
                val sexe = doc.getString("sexe") ?: "Homme"

                val civilite = if (sexe == "Femme") "Mme" else "M."
                welcomeText.text = "Bienvenue, $civilite $prenom !"


                // Choix de l'icône selon le sexe
                if (sexe == "Femme") {
                    profileIcon.setImageResource(R.drawable.icone_profile_fem)
                } else {
                    profileIcon.setImageResource(R.drawable.icone_profile_mal)
                }
            }

        //  Menu déroulant quand on clique sur l’icône profil
        /*profileIcon.setOnClickListener {
            val popup = PopupMenu(this, profileIcon)
            popup.menuInflater.inflate(R.menu.profile_menu, popup.menu)

            popup.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.menu_devenir_prestataire -> {
                        startActivity(Intent(this, BecomeProviderActivity::class.java))
                        true
                    }
                    R.id.menu_logout -> {
                        auth.signOut()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }*/
        profileIcon.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        //  Affichage dynamique des services
        val services = listOf(
            "Salle de sport",
            "Bibliothèque",
            "Ménage",
            "Santé",
            "Beauté",
            "Restauration"
        )

        /*for (service in services) {
            val btn = Button(this)
            btn.text = service
            btn.setOnClickListener {
                Toast.makeText(this, "Ouverture de $service", Toast.LENGTH_SHORT).show()
                // TODO: Intent vers l'activité correspondante
            }
            serviceGrid.addView(btn)
        }*/

        //  Affichage dynamique des services
        for (service in services) {
            val btn = Button(this)
            btn.text = service
            btn.setOnClickListener {
                when (service) {
                    //affichier la liste des services
                    //pour Restauration
                    "Restauration" -> {
                        startActivity(Intent(this, RestaurantListActivity::class.java))
                    }
                    else -> {
                        Toast.makeText(this, "Ouverture de $service", Toast.LENGTH_SHORT).show()
                        // TODO: Ajouter les autres Intents plus tard
                    }
                }
            }
            serviceGrid.addView(btn)
        }

    }
}
