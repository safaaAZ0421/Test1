package com.example.test1.models

data class LivreItem(
    val Titre: String = "",
    val Image: String = "",
    val Auteur: String = "",
    val Categorie: String = "",
    val Disponibilite: Boolean = true,
    val Emplacement: String = ""
)