package com.example.test1.model

data class Salon(
    var id: String = "",
    val nom: String = "",
    val adresse: String = "",
    val prixMoyen: Double = 0.0,
    val photos: List<String> = emptyList(),
    val description: String = ""
)