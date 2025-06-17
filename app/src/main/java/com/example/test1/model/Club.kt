package com.example.test1.model


data class Club(
    var id: String = "",
    val name: String = "",
    val disciplines: List<String> = emptyList(),
    val coaches: List<Coach> = emptyList(),
    val location: String = "",
    val photos: List<String> = emptyList(),
    val programs: List<Program> = emptyList(),
    val prices: Map<String, Double> = emptyMap()
)

data class User(
    val surname: String = "",
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val sexe : String = "",
    val telephone : String ="",
    val isMember: Boolean = false // Nouveau champ pour le statut d'adhésion
)

data class Coach(
    val id: String = "",
    val name: String = "",
    val discipline: String = "",
    val bio: String = "",
    val photoUrl: String = ""
)

data class Program(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val duration: String = ""
)