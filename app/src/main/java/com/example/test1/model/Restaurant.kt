package com.example.test1.model

import java.io.Serializable   //Serializable permet de passer l’objet Restaurant entre activités via Intent.

data class Restaurant(
    val id: String = "",
    val nom: String = "",
    val horaires: String = "",
    val adresse: String = "",
    val telephone: String = "",
    val imageUrl: String = "",
    val email: String = "",
    val instagram: String = "",
    val facebook: String = "",
    val siteWeb: String = "",
    val logoUrl: String = ""

) : Serializable