package com.example.test1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test1.R
import com.example.test1.models.LivreItem

class LivreAdapter(private val livresList: List<LivreItem>) :
    RecyclerView.Adapter<LivreAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.livreImageView)
        val titreTextView: TextView = itemView.findViewById(R.id.TitreTextView)
        val auteurTextView: TextView = itemView.findViewById(R.id.AuteurTextView)
        val categorieTextView: TextView = itemView.findViewById(R.id.CategorieTextView)
        val disponibiliteTextView: TextView = itemView.findViewById(R.id.DisponibiliteTextView)
        val emplacementTextView: TextView = itemView.findViewById(R.id.EmplacementTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_livre, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val livre = livresList[position]

        holder.titreTextView.text = livre.Titre
        holder.auteurTextView.text = "Auteur: ${livre.Auteur}"
        holder.categorieTextView.text = "Catégorie: ${livre.Categorie}"
        holder.disponibiliteTextView.text = if (livre.Disponibilite) "Disponible" else "Indisponible"
        holder.emplacementTextView.text = "Emplacement: ${livre.Emplacement}"

        Glide.with(holder.itemView.context)
            .load(livre.Image)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = livresList.size
}