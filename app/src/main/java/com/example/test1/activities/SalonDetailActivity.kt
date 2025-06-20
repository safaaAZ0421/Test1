package com.example.test1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.R
import com.example.test1.adapter.PhotoAdapter
import com.example.test1.model.Salon
import com.example.test1.services.SalonService

class SalonDetailActivity : AppCompatActivity() {

    private lateinit var textViewNom: TextView
    private lateinit var textViewAdresse: TextView
    private lateinit var textViewPrix: TextView
    private lateinit var textViewDescription: TextView
    private lateinit var recyclerViewPhotos: RecyclerView
    private lateinit var buttonReserver: Button
    private lateinit var photoAdapter: PhotoAdapter

    private val salonService = SalonService()
    private var salonId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salon_detail)

        salonId = intent.getStringExtra("salon_id") ?: ""

        initViews()
        loadSalonDetails()
    }

    private fun initViews() {
        textViewNom = findViewById(R.id.textViewNomSalon)
        textViewAdresse = findViewById(R.id.textViewAdresse)
        textViewPrix = findViewById(R.id.textViewPrix)
        textViewDescription = findViewById(R.id.textViewDescription)
        recyclerViewPhotos = findViewById(R.id.recyclerViewPhotos)
        buttonReserver = findViewById(R.id.buttonReserver)

        buttonReserver.setOnClickListener {
            val intent = Intent(this, AppointmentActivity::class.java)
            intent.putExtra("salon_id", salonId)
            startActivity(intent)
        }
    }

    private fun loadSalonDetails() {
        salonService.getSalonById(
            salonId = salonId,
            callback = { salon ->
                salon?.let { displaySalonDetails(it) }
            },
            onError = { exception ->
                // Gérer l'erreur
            }
        )
    }

    private fun displaySalonDetails(salon: Salon) {
        textViewNom.text = salon.nom
        textViewAdresse.text = salon.adresse
        textViewPrix.text = "Prix moyen: ${salon.prixMoyen}€"
        textViewDescription.text = salon.description

        // Configuration du RecyclerView pour les photos
        photoAdapter = PhotoAdapter(salon.photos)
        recyclerViewPhotos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewPhotos.adapter = photoAdapter
    }
}