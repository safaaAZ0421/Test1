/*package com.example.test1.adapters
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test1.R
import com.example.test1.models.BibliothequeItem
import com.example.test1.activities.BibliothequeDetailActivity



class BibliothequeAdapter(
    private val bibliothequesList: List<BibliothequeItem>
) : RecyclerView.Adapter<BibliothequeAdapter.BibliothequeViewHolder>() {

    class BibliothequeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nom: TextView = view.findViewById(R.id.nomBibliotheque)
       // val image: ImageView = view.findViewById(R.id.imageBibliotheque)
        //val adresse: TextView = view.findViewById(R.id.adresseBibliotheque)
        //val horaire: TextView = view.findViewById(R.id.horaireBibliotheque)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BibliothequeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bibliotheque, parent, false)
        return BibliothequeViewHolder(view)
    }

    override fun onBindViewHolder(holder: BibliothequeViewHolder, position: Int) {
        val bibliotheque = bibliothequesList[position]
        holder.nom.text = bibliotheque.nom
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, BibliothequeDetailActivity::class.java)
            intent.putExtra("nom", bibliotheque.nom)
            intent.putExtra("adresse", bibliotheque.adresse)
            intent.putExtra("horaire", bibliotheque.horaire)
            intent.putExtra("imageUrl", bibliotheque.imageUrl)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount() = bibliothequesList.size
}*/
/* deuxieme code
package com.example.test1.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.R
import com.example.test1.models.BibliothequeItem
import com.example.test1.activities.BibliothequeDetailActivity
import com.example.test1.activities.AdherentActivity

class BibliothequeAdapter(
    private val bibliothequesList: List<BibliothequeItem>
) : RecyclerView.Adapter<BibliothequeAdapter.BibliothequeViewHolder>() {

    class BibliothequeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nom: TextView = view.findViewById(R.id.nomBibliotheque)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BibliothequeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bibliotheque, parent, false)
        return BibliothequeViewHolder(view)
    }

    override fun onBindViewHolder(holder: BibliothequeViewHolder, position: Int) {
        val bibliotheque = bibliothequesList[position]
        holder.nom.text = bibliotheque.nom

        holder.itemView.setOnClickListener {
            // → Navigue vers AdherentActivity en passant l'ID de la bibliothèque
            val intent = Intent(holder.itemView.context, AdherentActivity::class.java)
            intent.putExtra("bibliothequeId", bibliotheque.id) // 🔥 clé nécessaire pour charger Salles et Livres
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = bibliothequesList.size
} */

/*package com.example.test1.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.R
import com.example.test1.models.BibliothequeItem
import com.example.test1.activities.BibliothequeDetailActivity
import com.example.test1.activities.AdherentActivity

class BibliothequeAdapter(
    private val bibliothequesList: List<BibliothequeItem>
) : RecyclerView.Adapter<BibliothequeAdapter.BibliothequeViewHolder>() {

    class BibliothequeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nom: TextView = view.findViewById(R.id.nomBibliotheque)
        // Si tu as un bouton dans item_bibliotheque.xml pour réserver:
        // val btnReserver: Button = view.findViewById(R.id.btnReserver)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BibliothequeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bibliotheque, parent, false)
        return BibliothequeViewHolder(view)
    }

    override fun onBindViewHolder(holder: BibliothequeViewHolder, position: Int) {
        val bibliotheque = bibliothequesList[position]
        holder.nom.text = bibliotheque.nom

        // Clic sur l’item pour aller vers BibliothequeDetailActivity (affichage info)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, BibliothequeDetailActivity::class.java)
            intent.putExtra("nom", bibliotheque.nom)
            intent.putExtra("adresse", bibliotheque.adresse)
            intent.putExtra("horaire", bibliotheque.horaire)
            intent.putExtra("imageUrl", bibliotheque.imageUrl)
            holder.itemView.context.startActivity(intent)
        }

        // Si tu veux un bouton pour réserver (aller vers AdherentActivity)
        /*
        holder.btnReserver.setOnClickListener {
            val intent = Intent(holder.itemView.context, AdherentActivity::class.java)
            intent.putExtra("bibliothequeId", bibliotheque.id)
            holder.itemView.context.startActivity(intent)
        }
        */
    }

    override fun getItemCount() = bibliothequesList.size
}*/
package com.example.test1.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.R
import com.example.test1.models.BibliothequeItem
import com.example.test1.activities.BibliothequeDetailActivity

class BibliothequeAdapter(
    private val bibliothequesList: List<BibliothequeItem>
) : RecyclerView.Adapter<BibliothequeAdapter.BibliothequeViewHolder>() {

    class BibliothequeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nom: TextView = view.findViewById(R.id.nomBibliotheque)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BibliothequeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bibliotheque, parent, false)
        return BibliothequeViewHolder(view)
    }

    override fun onBindViewHolder(holder: BibliothequeViewHolder, position: Int) {
        val bibliotheque = bibliothequesList[position]
        holder.nom.text = bibliotheque.nom

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, BibliothequeDetailActivity::class.java)

            // Envoi des infos de la bibliothèque + son ID Firestore
            intent.putExtra("nom", bibliotheque.nom)
            intent.putExtra("adresse", bibliotheque.adresse)
            intent.putExtra("horaire", bibliotheque.horaire)
            intent.putExtra("imageUrl", bibliotheque.imageUrl)
            intent.putExtra("bibliothequeId", bibliotheque.id)  // 👈 AJOUT ESSENTIEL

            context.startActivity(intent)
        }
    }

    override fun getItemCount() = bibliothequesList.size
}

