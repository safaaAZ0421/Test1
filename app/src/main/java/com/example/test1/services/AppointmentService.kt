package com.example.test1.services

import com.example.test1.model.Appointment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AppointmentService {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun createAppointment(appointment: Appointment, callback: (Boolean) -> Unit) {
        // Vérifier d\'abord la disponibilité
        checkAvailability(appointment.salonId, appointment.date, appointment.time) { isAvailable ->
            if (isAvailable) {
                db.collection("salons").document(appointment.salonId).collection("appointments")
                    .add(appointment)
                    .addOnSuccessListener {
                        callback(true)
                    }
                    .addOnFailureListener {
                        callback(false)
                    }
            } else {
                callback(false)
            }
        }
    }

    fun getUserAppointments(userId: String, callback: (List<Appointment>) -> Unit, onError: (Exception) -> Unit) {
        db.collectionGroup("appointments")
            .whereEqualTo("userId", userId)
            .whereEqualTo("status", "confirmée")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val appointments = mutableListOf<Appointment>()
                for (document in documents) {
                    val appointment = document.toObject(Appointment::class.java)
                    appointment.id = document.id
                    appointments.add(appointment)
                }
                callback(appointments)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun cancelAppointment(salonId: String, appointmentId: String, callback: (Boolean) -> Unit) {
        db.collection("salons").document(salonId).collection("appointments").document(appointmentId)
            .update("status", "annulée")
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun cancelAppointmentBySalonAndUser(salonId: String, userId: String, callback: (Boolean) -> Unit) {
        db.collection("salons").document(salonId).collection("appointments")
            .whereEqualTo("userId", userId)
            .whereEqualTo("status", "confirmée")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    callback(false)
                    return@addOnSuccessListener
                }

                var successCount = 0
                val totalDocs = documents.size()

                for (document in documents) {
                    document.reference.update("status", "annulée")
                        .addOnSuccessListener {
                            successCount++
                            if (successCount == totalDocs) {
                                callback(true)
                            }
                        }
                        .addOnFailureListener {
                            callback(false)
                        }
                }
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    private fun checkAvailability(salonId: String, date: String, time: String, callback: (Boolean) -> Unit) {
        db.collection("salons").document(salonId).collection("appointments")
            .whereEqualTo("date", date)
            .whereEqualTo("time", time)
            .whereEqualTo("status", "confirmée")
            .get()
            .addOnSuccessListener { documents ->
                // Si aucune réservation trouvée pour ce créneau, il est disponible
                callback(documents.isEmpty)
            }
            .addOnFailureListener {
                // En cas d\'erreur, considérer comme non disponible par sécurité
                callback(false)
            }
    }

    fun getAvailableTimeSlots(salonId: String, date: String, callback: (List<String>) -> Unit) {
        // Créneaux horaires disponibles (exemple: de 9h à 18h)
        val allTimeSlots = listOf(
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
            "15:00", "15:30", "16:00", "16:30", "17:00", "17:30"
        )

        db.collection("salons").document(salonId).collection("appointments")
            .whereEqualTo("date", date)
            .whereEqualTo("status", "confirmée")
            .get()
            .addOnSuccessListener { documents ->
                val bookedTimes = mutableSetOf<String>()
                for (document in documents) {
                    val appointment = document.toObject(Appointment::class.java)
                    bookedTimes.add(appointment.time)
                }

                val availableSlots = allTimeSlots.filter { it !in bookedTimes }
                callback(availableSlots)
            }
            .addOnFailureListener {
                // En cas d\'erreur, retourner une liste vide
                callback(emptyList())
            }
    }
}