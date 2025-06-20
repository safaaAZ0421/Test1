package com.example.test1.services

import com.example.test1.model.Salon
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class SalonService {
    private val db = FirebaseFirestore.getInstance()
    private val salonsCollection = db.collection("salons")

    fun getAllSalons(callback: (List<Salon>) -> Unit, onError: (Exception) -> Unit) {
        salonsCollection
            .orderBy("nom", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val salons = mutableListOf<Salon>()
                for (document in documents) {
                    val salon = document.toObject(Salon::class.java)
                    salon.id = document.id
                    salons.add(salon)
                }
                callback(salons)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun getSalonById(salonId: String, callback: (Salon?) -> Unit, onError: (Exception) -> Unit) {
        salonsCollection.document(salonId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val salon = document.toObject(Salon::class.java)
                    salon?.id = document.id
                    callback(salon)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun searchSalonsByName(query: String, callback: (List<Salon>) -> Unit, onError: (Exception) -> Unit) {
        salonsCollection
            .whereGreaterThanOrEqualTo("nom", query)
            .whereLessThanOrEqualTo("nom", query + '\uf8ff')
            .get()
            .addOnSuccessListener { documents ->
                val salons = mutableListOf<Salon>()
                for (document in documents) {
                    val salon = document.toObject(Salon::class.java)
                    salon.id = document.id
                    salons.add(salon)
                }
                callback(salons)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
}