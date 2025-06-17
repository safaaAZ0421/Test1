/*package com.example.test1.activities
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.test1.R

class ForfaitsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forfaits)

        val btnEtudiant = findViewById<Button>(R.id.btnEtudiant)
        val btnEmploye = findViewById<Button>(R.id.btnEmploye)
        val btnEnseignant = findViewById<Button>(R.id.btnEnseignant) // Modifié

        // Afficher les forfaits avec leurs prix
        btnEtudiant.text = "Forfait Étudiant - 150Dhs/mois"
        btnEmploye.text = "Forfait Employé - 300Dhs/mois"
        btnEnseignant.text = "Forfait Enseignants/Professeurs - 250Dhs/mois" // Modifié

        btnEtudiant.setOnClickListener {
            val intent = Intent(this, FormulaireAbonnementActivity::class.java)
            intent.putExtra("forfait", "Forfait Étudiant")
            intent.putExtra("prix", "150Dhs/mois")
            startActivity(intent)
        }

        btnEmploye.setOnClickListener {
            val intent = Intent(this, FormulaireAbonnementActivity::class.java)
            intent.putExtra("forfait", "Forfait Employé")
            intent.putExtra("prix", "300Dhs/mois")
            startActivity(intent)
        }

        btnEnseignant.setOnClickListener { // Modifié
            val intent = Intent(this, FormulaireAbonnementActivity::class.java)
            intent.putExtra("forfait", "Forfait Enseignants/Professeurs") // Modifié
            intent.putExtra("prix", "250Dhs/mois")
            startActivity(intent)
        }
    }
}*/
package com.example.test1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.test1.R

class ForfaitsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forfaits)

        val btnEtudiant = findViewById<Button>(R.id.btnEtudiant)
        val btnEmploye = findViewById<Button>(R.id.btnEmploye)
        val btnEnseignant = findViewById<Button>(R.id.btnEnseignant)

        // Récupérer l'id de la bibliothèque transmis depuis BibliothequeDetailActivity
        val bibliothequeId = intent.getStringExtra("bibliothequeId")

        // Afficher les forfaits avec leurs prix
        btnEtudiant.text = "Forfait Étudiant - 150Dhs/mois"
        btnEmploye.text = "Forfait Employé - 300Dhs/mois"
        btnEnseignant.text = "Forfait Enseignants/Professeurs - 250Dhs/mois"

        btnEtudiant.setOnClickListener {
            val intent = Intent(this, FormulaireAbonnementActivity::class.java)
            intent.putExtra("forfait", "Forfait Étudiant")
            intent.putExtra("prix", "150Dhs/mois")
            intent.putExtra("bibliothequeId", bibliothequeId) // 👈 Ajout important
            startActivity(intent)
        }

        btnEmploye.setOnClickListener {
            val intent = Intent(this, FormulaireAbonnementActivity::class.java)
            intent.putExtra("forfait", "Forfait Employé")
            intent.putExtra("prix", "300Dhs/mois")
            intent.putExtra("bibliothequeId", bibliothequeId) // 👈 Ajout important
            startActivity(intent)
        }

        btnEnseignant.setOnClickListener {
            val intent = Intent(this, FormulaireAbonnementActivity::class.java)
            intent.putExtra("forfait", "Forfait Enseignants/Professeurs")
            intent.putExtra("prix", "250Dhs/mois")
            intent.putExtra("bibliothequeId", bibliothequeId) // 👈 Ajout important
            startActivity(intent)
        }
    }
}
