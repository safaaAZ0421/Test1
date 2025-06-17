/*package com.example.test1.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test1.R
import com.example.test1.models.Salles

class SalleAdapter(private val sallesList: List<Salles>, private val context: Context) :
    RecyclerView.Adapter<SalleAdapter.ViewHolder>() {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("Reservations", Context.MODE_PRIVATE)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val salleImageView: ImageView = itemView.findViewById(R.id.salleImageView)
        val nomTextView: TextView = itemView.findViewById(R.id.nomTextView)
        val capaciteTextView: TextView = itemView.findViewById(R.id.capaciteTextView)
        val disponibiliteTextView: TextView = itemView.findViewById(R.id.disponibiliteTextView)
        val btnReserver: Button = itemView.findViewById(R.id.btnReserver)
        val btnAnnuler: Button = itemView.findViewById(R.id.btnAnnulerAbonnement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_salle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val salle = sallesList[position]
        holder.nomTextView.text = salle.Identifiant


        // Affichage des données
        holder.nomTextView.text = salle.Identifiant
        holder.capaciteTextView.text = "Capacité: ${salle.Capacite} personnes"
        holder.disponibiliteTextView.text =
            if (salle.Disponibilite) "Disponible" else "Occupée"

        Glide.with(holder.itemView.context)
            .load(salle.Image)
            .into(holder.salleImageView)

        // Vérifier si l'utilisateur a déjà réservé une salle
        val salleReservee = sharedPreferences.getString("salle_reservee", null)

        holder.btnReserver.setOnClickListener {
            if (salleReservee == null) {
                // Enregistrer la réservation
                sharedPreferences.edit().putString("salle_reservee", salle.Identifiant).apply()
                Toast.makeText(context, "Salle ${salle.Identifiant} réservée!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Vous avez déjà réservé une salle ($salleReservee).", Toast.LENGTH_LONG).show()
            }
        }

        holder.btnAnnuler.setOnClickListener {
            if (salleReservee == salle.Identifiant) {
                sharedPreferences.edit().remove("salle_reservee").apply()
                Toast.makeText(context, "Réservation annulée pour ${salle.Identifiant}.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Vous n'avez pas réservé cette salle.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = sallesList.size
}*/

package com.example.test1.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test1.R
import com.example.test1.models.Salles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class SalleAdapter(
    private val sallesList: List<Salles>,
    private val context: Context,
    private val bibliothequeId: String
) : RecyclerView.Adapter<SalleAdapter.ViewHolder>() {

    private val db = FirebaseFirestore.getInstance()
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val salleImageView: ImageView = itemView.findViewById(R.id.salleImageView)
        val nomTextView: TextView = itemView.findViewById(R.id.nomTextView)
        val capaciteTextView: TextView = itemView.findViewById(R.id.capaciteTextView)
        val disponibiliteTextView: TextView = itemView.findViewById(R.id.disponibiliteTextView)
        val btnReserver: Button = itemView.findViewById(R.id.btnReserver)
        val btnAnnuler: Button = itemView.findViewById(R.id.btnAnnulerAbonnement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_salle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val salle = sallesList[position]

        holder.nomTextView.text = salle.salleId
        holder.capaciteTextView.text = "Capacité: ${salle.Capacite} personnes"
        holder.disponibiliteTextView.text = if (salle.Disponibilite) "Disponible" else "Occupée"

        Glide.with(holder.itemView.context)
            .load(salle.Image)
            .into(holder.salleImageView)

        holder.btnReserver.setOnClickListener {
            if (currentUserId == null) {
                Toast.makeText(context, "Utilisateur non connecté", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val salleId = salle.salleId ?: return@setOnClickListener

            // Vérifie si l'utilisateur a déjà réservé cette salle
            db.collection("Bibliotheques")
                .document(bibliothequeId)
                .collection("Reservations")
                .whereEqualTo("userId", currentUserId)
                .whereEqualTo("salleId", salleId)
                .get()
                .addOnSuccessListener { docs ->
                    if (!docs.isEmpty) {
                        Toast.makeText(context, "Vous avez déjà réservé cette salle.", Toast.LENGTH_SHORT).show()
                    } else {
                        // Ici on pourrait demander à l'utilisateur de choisir une heure précise
                        // Pour simplifier, on met l'heure actuelle au format String
                        val dateNow = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val heureNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

                        val reservation = hashMapOf(
                            "userId" to currentUserId,
                            "salleId" to salleId,
                            "nomSalle" to salle.salleId,
                            "dateReservation" to dateNow,
                            "heureReservation" to heureNow,
                            "timestamp" to System.currentTimeMillis()
                        )

                        db.collection("Bibliotheques")
                            .document(bibliothequeId)
                            .collection("Reservations")
                            .add(reservation)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Réservation effectuée", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Erreur: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                }
        }

        holder.btnAnnuler.setOnClickListener {
            if (currentUserId == null) {
                Toast.makeText(context, "Utilisateur non connecté", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val salleId = salle.salleId ?: return@setOnClickListener

            db.collection("Bibliotheques")
                .document(bibliothequeId)
                .collection("Reservations")
                .whereEqualTo("userId", currentUserId)
                .whereEqualTo("salleId", salleId)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        for (doc in documents) {
                            db.collection("Bibliotheques")
                                .document(bibliothequeId)
                                .collection("Reservations")
                                .document(doc.id)
                                .delete()
                        }
                        Toast.makeText(context, "Réservation annulée", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Aucune réservation trouvée pour cette salle", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Erreur : ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun getItemCount(): Int = sallesList.size
}
