package com.example.test1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test1.R
import com.example.test1.model.Restaurant

class RestaurantAdapter(
    //private val restaurantList: List<Restaurant>,
    private var restaurantList: List<Restaurant>,
    private val onClick: (Restaurant) -> Unit
) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.bind(restaurant)
    }


    fun updateList(newList: List<Restaurant>) {
        restaurantList = newList
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = restaurantList.size

    inner class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.restaurantName)
        //private val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        val logoImageView: ImageView = itemView.findViewById(R.id.restaurantLogo)


        fun bind(restaurant: Restaurant) {
            nameTextView.text = restaurant.nom

            // Charger le logo avec Glide, mettre une image par défaut en cas d'erreur ou d'URL vide
            Glide.with(itemView.context)
                .load(restaurant.imageUrl) // Remplace par restaurant.logoUrl si tu as un champ dédié
                .placeholder(R.drawable.ic_launcher_foreground) // image par défaut pendant chargement
                .error(R.drawable.ic_launcher_foreground)       // image par défaut si erreur
                .into(logoImageView)

            // Gestion du clic
            itemView.setOnClickListener { onClick(restaurant) }
        }
    }
}
