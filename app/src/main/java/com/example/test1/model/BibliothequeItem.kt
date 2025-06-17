package com.example.test1.models
import java.io.Serializable

data class BibliothequeItem(
    val nom: String = "",
    val adresse: String = "",
    val horaire: String = "",
    val imageUrl: String = "",
    val id: String = "" // ← ID du document Firestore (ex: "Biblio1")
) :Serializable