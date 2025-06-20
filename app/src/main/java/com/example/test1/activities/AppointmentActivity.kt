package com.example.test1.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test1.R
import com.example.test1.model.Appointment
import com.example.test1.services.AppointmentService
import com.example.test1.services.SalonService
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class AppointmentActivity : AppCompatActivity() {

    private lateinit var textViewSalonNom: TextView
    private lateinit var textViewDateChoisie: TextView
    private lateinit var textViewHeureChoisie: TextView
    private lateinit var buttonChoisirDate: Button
    private lateinit var buttonChoisirHeure: Button
    private lateinit var buttonConfirmerRendezVous: Button
    private lateinit var buttonAnnulerRendezVous: Button

    private val appointmentService = AppointmentService()
    private val salonService = SalonService()
    private val auth = FirebaseAuth.getInstance()
    private var salonId: String = ""
    private var selectedDate: String = ""
    private var selectedTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        salonId = intent.getStringExtra("salon_id") ?: ""

        initViews()
        loadSalonName()
        setupClickListeners()
    }

    private fun initViews() {
        textViewSalonNom = findViewById(R.id.textViewSalonNom)
        textViewDateChoisie = findViewById(R.id.textViewDateChoisie)
        textViewHeureChoisie = findViewById(R.id.textViewHeureChoisie)
        buttonChoisirDate = findViewById(R.id.buttonChoisirDate)
        buttonChoisirHeure = findViewById(R.id.buttonChoisirHeure)
        buttonConfirmerRendezVous = findViewById(R.id.buttonConfirmerRendezVous)
        buttonAnnulerRendezVous = findViewById(R.id.buttonAnnulerRendezVous)
    }

    private fun setupClickListeners() {
        buttonChoisirDate.setOnClickListener { showDatePicker() }
        buttonChoisirHeure.setOnClickListener { showTimePicker() }
        buttonConfirmerRendezVous.setOnClickListener { confirmerRendezVous() }
        buttonAnnulerRendezVous.setOnClickListener { annulerRendezVous() }
    }

    /*private fun loadSalonName() {
        salonService.getSalonById(
            salonId = salonId,
            callback = { salon ->
                salon?.let {
                    textViewSalonNom.text = "Rendez-vous pour: ${it.nom}"
                }
            },
            onError = { exception ->
                // Gérer l'erreur
            }
        )
    }*/

    private fun loadSalonName() {
        // 1. Validate salonId
        if (salonId.isNullOrEmpty()) {
            textViewSalonNom?.text = "Salon non spécifié"
            return
        }

        // 2. Show loading state
        textViewSalonNom?.text = "Chargement..."

        // 3. Make service call
        salonService.getSalonById(
            salonId = salonId,
            callback = { salon ->
                runOnUiThread {
                    when {
                        salon == null -> {
                            textViewSalonNom?.text = "Salon non trouvé"
                        }
                        salon.nom.isNullOrEmpty() -> {
                            textViewSalonNom?.text = "Nom non disponible"
                        }
                        else -> {
                            textViewSalonNom?.text = "Rendez-vous pour: ${salon.nom}"
                        }
                    }
                }
            },
            onError = { exception ->
                runOnUiThread {
                    textViewSalonNom?.text = "Erreur de chargement"
                    Log.e("SalonName", "Error loading salon: $salonId", exception)
                    Toast.makeText(
                       this,
                        "Impossible de charger les informations du salon",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate = "$dayOfMonth/${month + 1}/$year"
                textViewDateChoisie.text = "Date: $selectedDate"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                textViewHeureChoisie.text = "Heure: $selectedTime"
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun confirmerRendezVous() {
        if (selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Veuillez choisir une date et une heure", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid ?: return
        val appointment = Appointment(
            userId = userId,
            salonId = salonId,
            date = selectedDate,
            time = selectedTime,
            status = "confirmée"
        )

        appointmentService.createAppointment(appointment) { success ->
            if (success) {
                Toast.makeText(this, "Rendez-vous confirmé!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Erreur lors de la prise de rendez-vous", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun annulerRendezVous() {
        val userId = auth.currentUser?.uid ?: return

        appointmentService.cancelAppointmentBySalonAndUser(salonId, userId) { success ->
            if (success) {
                Toast.makeText(this, "Rendez-vous annulé", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Erreur lors de l'annulation", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
