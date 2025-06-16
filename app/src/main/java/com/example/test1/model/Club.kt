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