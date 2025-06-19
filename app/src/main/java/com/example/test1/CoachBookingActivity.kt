package com.example.test1

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test1.adapter.TimeSlot
import com.example.test1.adapter.TimeSlotAdapter
import com.example.test1.model.Club
import com.example.test1.model.Coach
import com.example.test1.model.Reservation
import com.google.android.material.button.MaterialButton
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CoachBookingActivity : AppCompatActivity() {

    // Views
    private lateinit var imageViewCoach: CircleImageView
    private lateinit var textViewCoachName: TextView
    private lateinit var textViewCoachSpecialty: TextView
    private lateinit var textViewCoachExperience: TextView
    private lateinit var textViewCoachPrice: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var recyclerViewTimeSlots: RecyclerView
    private lateinit var textViewNoSlots: TextView
    private lateinit var cardViewBookingSummary: CardView
    private lateinit var textViewSelectedDate: TextView
    private lateinit var textViewSelectedTime: TextView
    private lateinit var textViewTotalPrice: TextView
    private lateinit var buttonConfirmBooking: MaterialButton
    private lateinit var progressBarLoading: ProgressBar

    // Data
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var timeSlotAdapter: TimeSlotAdapter

    private var selectedCoach: Coach? = null
    private var selectedClub: Club? = null
    private var selectedDate: Date? = null
    private var selectedTimeSlot: TimeSlot? = null

    private val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coach_booking)

        // Récupérer les données passées depuis l'activité précédente
        val coachId = intent.getStringExtra("coach_id")
        val clubId = intent.getStringExtra("club_id")

        initViews()
        setupToolbar()
        setupAuth()
        setupRecyclerView()
        setupCalendar()
        setupClickListeners()

        // Charger les données du coach et du club
        loadCoachAndClubData(coachId, clubId)
    }

    private fun initViews() {
        imageViewCoach = findViewById(R.id.imageViewCoach)
        textViewCoachName = findViewById(R.id.textViewCoachName)
        textViewCoachSpecialty = findViewById(R.id.textViewCoachSpecialty)
        textViewCoachExperience = findViewById(R.id.textViewCoachExperience)
        textViewCoachPrice = findViewById(R.id.textViewCoachPrice)
        calendarView = findViewById(R.id.calendarView)
        recyclerViewTimeSlots = findViewById(R.id.recyclerViewTimeSlots)
        textViewNoSlots = findViewById(R.id.textViewNoSlots)
        cardViewBookingSummary = findViewById(R.id.cardViewBookingSummary)
        textViewSelectedDate = findViewById(R.id.textViewSelectedDate)
        textViewSelectedTime = findViewById(R.id.textViewSelectedTime)
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice)
        buttonConfirmBooking = findViewById(R.id.buttonConfirmBooking)
        progressBarLoading = findViewById(R.id.progressBarLoading)
    }

    private fun setupToolbar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupAuth() {
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    private fun setupRecyclerView() {
        timeSlotAdapter = TimeSlotAdapter(emptyList()) { timeSlot ->
            onTimeSlotSelected(timeSlot)
        }

        recyclerViewTimeSlots.apply {
            layoutManager = LinearLayoutManager(this@CoachBookingActivity)
            adapter = timeSlotAdapter
        }
    }

    private fun setupCalendar() {
        // Définir la date minimum à aujourd'hui
        calendarView.minDate = System.currentTimeMillis()

        // Définir la date maximum à 3 mois dans le futur
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 3)
        calendarView.maxDate = calendar.timeInMillis

        // Gérer la sélection de date
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.time

            loadAvailableTimeSlots()
        }
    }

    private fun setupClickListeners() {
        buttonConfirmBooking.setOnClickListener {
            confirmBooking()
        }
    }

    private fun loadCoachAndClubData(coachId: String?, clubId: String?) {
        if (coachId == null || clubId == null) {
            showError("Données manquantes")
            return
        }

        showLoading(true)

        // Charger les données du club d'abord
        firestore.collection("clubs").document(clubId)
            .get()
            .addOnSuccessListener { clubDocument ->
                if (clubDocument.exists()) {
                    selectedClub = clubDocument.toObject(Club::class.java)
                    selectedClub?.id = clubDocument.id

                    // Trouver le coach dans la liste des coaches du club
                    selectedCoach = selectedClub?.coaches?.find { it.id == coachId }

                    if (selectedCoach != null) {
                        displayCoachInfo()
                        showLoading(false)
                    } else {
                        showError("Coach non trouvé")
                        showLoading(false)
                    }
                } else {
                    showError("Club non trouvé")
                    showLoading(false)
                }
            }
            .addOnFailureListener { exception ->
                showError("Erreur lors du chargement: ${exception.message}")
                showLoading(false)
            }
    }

    private fun displayCoachInfo() {
        selectedCoach?.let { coach ->
            textViewCoachName.text = coach.name
            textViewCoachSpecialty.text = coach.discipline
            textViewCoachExperience.text = "${coach.bio} ans d'expérience"
            textViewCoachPrice.text = "${coach.hourlyRate.toInt()}Dh/h"

            // Charger la photo du coach
            if (coach.photoUrl.isNotEmpty()) {
                Glide.with(this)
                    .load(coach.photoUrl)
                    .placeholder(R.drawable.ic_person_default)
                    .error(R.drawable.ic_person_default)
                    .into(imageViewCoach)
            } else {
                imageViewCoach.setImageResource(R.drawable.ic_person_default)
            }
        }
    }

    private fun loadAvailableTimeSlots() {
        selectedDate?.let { date ->
            showLoading(true)

            // Générer des créneaux horaires factices pour la démo
            // Dans une vraie application, vous chargeriez les créneaux depuis Firestore
            generateDemoTimeSlots(date)
        }
    }

    private fun generateDemoTimeSlots(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = date

        val timeSlots = mutableListOf<TimeSlot>()
        val hourlyRate = selectedCoach?.hourlyRate ?: 35.0

        // Générer des créneaux de 9h à 18h
        for (hour in 9..17) {
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val startTime = calendar.time

            calendar.add(Calendar.HOUR, 1)
            val endTime = calendar.time

            // Simuler la disponibilité (70% des créneaux sont disponibles)
            val isAvailable = (1..10).random() <= 7

            timeSlots.add(TimeSlot(startTime, endTime, hourlyRate, isAvailable))

            // Remettre le calendrier à l'heure de début pour le prochain créneau
            calendar.add(Calendar.HOUR, -1)
        }

        // Mettre à jour l'adaptateur
        timeSlotAdapter.updateTimeSlots(timeSlots)

        // Afficher ou masquer le message "Aucun créneau"
        if (timeSlots.isEmpty() || timeSlots.none { it.isAvailable }) {
            textViewNoSlots.visibility = View.VISIBLE
            recyclerViewTimeSlots.visibility = View.GONE
        } else {
            textViewNoSlots.visibility = View.GONE
            recyclerViewTimeSlots.visibility = View.VISIBLE
        }

        showLoading(false)
    }

    private fun onTimeSlotSelected(timeSlot: TimeSlot) {
        selectedTimeSlot = timeSlot
        updateBookingSummary()
    }

    private fun updateBookingSummary() {
        if (selectedDate != null && selectedTimeSlot != null) {
            cardViewBookingSummary.visibility = View.VISIBLE

            textViewSelectedDate.text = dateFormat.format(selectedDate!!)

            val startTimeStr = timeFormat.format(selectedTimeSlot!!.startTime)
            val endTimeStr = timeFormat.format(selectedTimeSlot!!.endTime)
            textViewSelectedTime.text = "$startTimeStr - $endTimeStr"

            textViewTotalPrice.text = "${selectedTimeSlot!!.price.toInt()}€"

            buttonConfirmBooking.isEnabled = true
        } else {
            cardViewBookingSummary.visibility = View.GONE
            buttonConfirmBooking.isEnabled = false
        }
    }

    private fun confirmBooking() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            showError("Vous devez être connecté pour effectuer une réservation")
            return
        }

        if (selectedCoach == null || selectedClub == null || selectedDate == null || selectedTimeSlot == null) {
            showError("Veuillez sélectionner tous les éléments requis")
            return
        }

        showLoading(true)

        // Créer la réservation
        val reservation = Reservation(
            userId = currentUser.uid,
            coachId = selectedCoach!!.id,
            clubId = selectedClub!!.id,
            discipline = selectedCoach!!.discipline,
            startTime = Timestamp(selectedTimeSlot!!.startTime),
            endTime = Timestamp(selectedTimeSlot!!.endTime),
            status = "confirmed",
            price = selectedTimeSlot!!.price,
            createdAt = Timestamp.now()
        )

        // Sauvegarder dans Firestore
        firestore.collection("reservations")
            .add(reservation)
            .addOnSuccessListener { documentReference ->
                showLoading(false)
                showSuccess("Réservation confirmée avec succès!")

                // Retourner à l'activité précédente
                finish()
            }
            .addOnFailureListener { exception ->
                showLoading(false)
                showError("Erreur lors de la réservation: ${exception.message}")
            }
    }

    private fun showLoading(show: Boolean) {
        progressBarLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}