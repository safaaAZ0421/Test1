package com.example.test1.models
import java.io.Serializable

data class Salles(
    val salleId: String = "",
    val Image: String = "",
    val Capacite: Int = 0,
    val Disponibilite: Boolean = true,
    val nomSalle: String = "",
    val id: String = ""
):Serializable