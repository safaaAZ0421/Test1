package com.example.test1.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test1.R
import com.example.test1.model.Club

class ClubAdapter(
    private var clubs: List<Club>,
    private val onClubClick: (Club) -> Unit
) : RecyclerView.Adapter<ClubAdapter.ClubViewHolder>() {

    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewClub: ImageView = itemView.findViewById(R.id.imageViewClub)
        val textViewClubName: TextView = itemView.findViewById(R.id.textViewClubName)
        val textViewLocation: TextView = itemView.findViewById(R.id.textViewLocation)
        val textViewDisciplines: TextView = itemView.findViewById(R.id.textViewDisciplines)
        val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_club, parent, false)
        return ClubViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        val club = clubs[position]

        holder.textViewClubName.text = club.name
        holder.textViewLocation.text = club.location
        holder.textViewDisciplines.text = club.disciplines.joinToString(" • ")

        // Afficher le prix minimum
        val minPrice = club.prices.values.minOrNull()
        holder.textViewPrice.text = if (minPrice != null) {
            "À partir de ${minPrice.toInt()}€/mois"
        } else {
            "Prix sur demande"
        }

        // Charger l'image du club
        if (club.photos.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(club.photos.first())
                .placeholder(R.drawable.ic_sports_default)
                .error(R.drawable.ic_sports_default)
                .into(holder.imageViewClub)
        } else {
            holder.imageViewClub.setImageResource(R.drawable.ic_sports_default)
        }

        // Gérer le clic sur l'élément
        holder.itemView.setOnClickListener {
            onClubClick(club)
        }
    }

    override fun getItemCount(): Int = clubs.size

    fun updateClubs(newClubs: List<Club>) {
        clubs = newClubs
        notifyDataSetChanged()
    }
}