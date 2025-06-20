package com.example.test1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.R
import com.example.test1.model.Salon
import com.squareup.picasso.Picasso

class SalonAdapter(
    private val salons: List<Salon>,
    private val onSalonClick: (Salon) -> Unit
) : RecyclerView.Adapter<SalonAdapter.SalonViewHolder>() {

    class SalonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewSalon: ImageView = itemView.findViewById(R.id.imageViewSalon)
        val textViewNomSalon: TextView = itemView.findViewById(R.id.textViewNomSalon)
        val textViewAdresse: TextView = itemView.findViewById(R.id.textViewAdresse)
        val textViewPrix: TextView = itemView.findViewById(R.id.textViewPrix)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_salon, parent, false)
        return SalonViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalonViewHolder, position: Int) {
        val salon = salons[position]

        holder.textViewNomSalon.text = salon.nom
        holder.textViewAdresse.text = salon.adresse
        holder.textViewPrix.text = "Prix moyen: ${salon.prixMoyen}€"

        // Charger la première photo si disponible
        if (salon.photos.isNotEmpty()) {
            Picasso.get()
                .load(salon.photos[0])
                .placeholder(R.drawable.ic_salon_placeholder)
                .into(holder.imageViewSalon)
        }

        holder.itemView.setOnClickListener {
            onSalonClick(salon)
        }
    }

    override fun getItemCount(): Int = salons.size
}
