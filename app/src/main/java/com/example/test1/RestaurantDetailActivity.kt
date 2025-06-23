package com.example.test1

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.test1.model.Restaurant
import com.google.firebase.auth.FirebaseAuth
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore


class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var restaurant: Restaurant

    // Simule l'enregistrement d'une réservation
    private fun saveReservation(restaurantId: String, date: String, guests: Int) {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            Toast.makeText(this, "Utilisateur non authentifié", Toast.LENGTH_SHORT).show()
            return
        }

        val reservationData = hashMapOf(
            "userId" to user.uid,
            "date" to date,
            "guests" to guests
        )

        db.collection("Restaurants")
            .document(restaurantId)
            .collection("reservations")
            .add(reservationData)
            .addOnSuccessListener {
                Toast.makeText(this, "Réservation enregistrée", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erreur lors de la réservation : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    // Simule l'annulation de réservation
    private fun deleteReservation(restaurantId: String, reservationId: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("Restaurants")
            .document(restaurantId)
            .collection("reservations")
            .document(reservationId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Réservation annulée", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erreur lors de l’annulation : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vérifie la session
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            Toast.makeText(this, "Session expirée, retour à l'accueil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_restaurant_detail)

        // Récupération des vues
        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val addressTextView: TextView = findViewById(R.id.addressTextView)
        val horairesTextView: TextView = findViewById(R.id.horairesTextView)
        val restaurantImageView: ImageView = findViewById(R.id.restaurantImageView)
        val reserveButton: Button = findViewById(R.id.reserveButton)
        val websiteButton: Button = findViewById(R.id.websiteButton)
        val instagramButton: ImageButton = findViewById(R.id.instagramButton)
        val facebookButton:  ImageButton = findViewById(R.id.facebookButton)

        val reservationSection: LinearLayout = findViewById(R.id.reservationSection)
        val guestsPicker: NumberPicker = findViewById(R.id.guestsPicker)
        val selectDateButton: Button = findViewById(R.id.selectDateButton)
        val selectedDateText: TextView = findViewById(R.id.selectedDateText)
        val timePicker: TimePicker = findViewById(R.id.timePicker)
        timePicker.setIs24HourView(true) // ou false pour AM/PM

        val cancelButton: Button = findViewById(R.id.cancelButton)
        val bookButton: Button = findViewById(R.id.bookButton)

        // Configurer les valeurs du NumberPicker
        guestsPicker.minValue = 1
        guestsPicker.maxValue = 10

        // Récupérer le restaurant
        restaurant = intent.getSerializableExtra("restaurant") as? Restaurant
            ?: run {
                Toast.makeText(this, "Erreur : Restaurant non trouvé", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

        // Affichage des infos
        nameTextView.text = restaurant.nom
        addressTextView.text = restaurant.adresse
        horairesTextView.text = restaurant.horaires

        //Image du restaurant
        val imageUrl = restaurant.imageUrl // Assure-toi que ce champ existe

        Glide.with(this)
            .load(imageUrl)
            //.placeholder(R.drawable.placeholder_image)
            //.error(R.drawable.error_image)
            .into(restaurantImageView)


        // Affiche la section de réservation quand on clique
        reserveButton.setOnClickListener {
            reservationSection.visibility = View.VISIBLE
        }


        // Sélecteur de date
        selectDateButton.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    selectedDateText.text = selectedDate
                },
                2025, 5, 24
            )
            datePickerDialog.show()
        }

        // Annuler la réservation (cacher section)
        cancelButton.setOnClickListener {
            reservationSection.visibility = View.GONE
            selectedDateText.text = "Aucune date sélectionnée"
        }

        // Valider la réservation
        bookButton.setOnClickListener {
            val selectedDate = selectedDateText.text.toString()
            val guests = guestsPicker.value

            if (selectedDate.contains("Aucune")) {
                Toast.makeText(this, "Veuillez sélectionner une date", Toast.LENGTH_SHORT).show()
            } else {
                saveReservation(
                    restaurant.id,
                    selectedDate,
                    guests
                )

                reservationSection.visibility = View.GONE
            }
        }

        // Site web
        websiteButton.setOnClickListener {
            val url = restaurant.siteWeb
            if (url.isNotEmpty() && (url.startsWith("http://") || url.startsWith("https://"))) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Lien du site non disponible ou invalide", Toast.LENGTH_SHORT).show()
            }
        }

        // Instagram
        instagramButton.setOnClickListener {
            var instaUrl = restaurant.instagram.trim()
            if (!instaUrl.startsWith("http://") && !instaUrl.startsWith("https://") && instaUrl.isNotEmpty()) {
                instaUrl = "https://instagram.com/$instaUrl"
            }

            if (instaUrl.isNotEmpty() && (instaUrl.startsWith("http://") || instaUrl.startsWith("https://"))) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instaUrl))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Lien Instagram non disponible ou invalide", Toast.LENGTH_SHORT).show()
            }
        }

        // Facebook
        facebookButton.setOnClickListener {
            var faceUrl = restaurant.facebook.trim()
            if (!faceUrl.startsWith("http://") && !faceUrl.startsWith("https://") && faceUrl.isNotEmpty()) {
                faceUrl = "https://facebook.com/$faceUrl"
            }

            if (faceUrl.isNotEmpty() && (faceUrl.startsWith("http://") || faceUrl.startsWith("https://"))) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(faceUrl))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Lien Facebook non disponible ou invalide", Toast.LENGTH_SHORT).show()
            }
        }


    }

}
