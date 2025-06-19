package com.example.test1.model

import com.google.firebase.Timestamp

data class Reservation(
    val id: String = "",
    val userId: String = "",
    val coachId: String = "",
    val clubId: String = "",
    val discipline: String = "",
    val startTime: Timestamp? = null,
    val endTime: Timestamp? = null,
    val status: String = "pending", // e.g., "pending", "confirmed", "cancelled", "completed"
    val price: Double = 0.0,
    val createdAt: Timestamp? = null
)

data class CoachAvailability(
    val id: String = "",
    val coachId: String = "",
    val clubId: String = "",
    val startTime: Timestamp? = null,
    val endTime: Timestamp? = null,
    val isBooked: Boolean = false
)