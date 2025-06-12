package com.example.test1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PrestataireHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prestataire_home)
        Log.d("DEBUG", "PrestataireHomeActivity onCreate lancé")
        Toast.makeText(this, "Espace Prestataire ouvert", Toast.LENGTH_LONG).show()



        /*val btnAjouter = findViewById<Button>(R.id.btnAjouterService)
        val btnMesServices = findViewById<Button>(R.id.btnMesServices)
        val btnRetour = findViewById<Button>(R.id.btnRetourProfil)

        // Rediriger vers activité d'ajout de service (à créer)
        btnAjouter.setOnClickListener {
            // startActivity(Intent(this, AjouterServiceActivity::class.java))
        }

        // Rediriger vers la liste des services
        btnMesServices.setOnClickListener {
            // startActivity(Intent(this, ListeServicesActivity::class.java))
        }

        // Revenir au profil
        btnRetour.setOnClickListener {
            finish()
        }*/
    }
}
