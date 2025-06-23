package com.example.test1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.adapter.RestaurantAdapter
import com.example.test1.model.Restaurant
import com.google.firebase.firestore.FirebaseFirestore

class RestaurantListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter
    private val restaurantList = ArrayList<Restaurant>()
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private var allRestaurants = listOf<Restaurant>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)
        searchView = findViewById(R.id.searchView)

        recyclerView = findViewById(R.id.restaurantRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        restaurantAdapter = RestaurantAdapter(restaurantList) { restaurant ->
            val intent = Intent(this, RestaurantDetailActivity::class.java)
            intent.putExtra("restaurant", restaurant)
            startActivity(intent)
        }
        recyclerView.adapter = restaurantAdapter

        // Récupérer les restaurants depuis Firestore
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("Restaurants")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                   // Log.e("Firestore", "Erreur : ${error.message}")
                    //Toast.makeText(this, "Erreur de connexion à Firestore", Toast.LENGTH_SHORT).show()
                    //return@addSnapshotListener
                    Log.e("Firestore", "Erreur complète : ", error)
                    Toast.makeText(this, "Erreur Firestore : ${error.message}", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                /*restaurantList.clear()
                snapshot?.documents?.forEach { document ->
                    val restaurantData = document.data
                    val restaurant = Restaurant(
                        id = document.id,
                        nom = restaurantData?.get("nom") as? String ?: "",
                        horaires = restaurantData?.get("horaires") as? String ?: "",
                        adresse = restaurantData?.get("adresse") as? String ?: "",
                        telephone = restaurantData?.get("telephone") as? String ?: "",
                        imageUrl = restaurantData?.get("imageUrl") as? String ?: "",
                        email = restaurantData?.get("email") as? String ?: "",
                        instagram = restaurantData?.get("instagram") as? String ?: "",
                        siteWeb = restaurantData?.get("siteWeb") as? String ?: "",
                        logoUrl = restaurantData?.get("logoUrl") as? String ?: ""
                    )
                    restaurantList.add(restaurant)
                }
                restaurantAdapter.notifyDataSetChanged()*/

                restaurantList.clear()
                allRestaurants = snapshot?.documents?.map { document ->
                    val restaurantData = document.data
                    Restaurant(
                        id = document.id,
                        nom = restaurantData?.get("nom") as? String ?: "",
                        horaires = restaurantData?.get("horaires") as? String ?: "",
                        adresse = restaurantData?.get("adresse") as? String ?: "",
                        telephone = restaurantData?.get("telephone") as? String ?: "",
                        imageUrl = restaurantData?.get("imageUrl") as? String ?: "",
                        email = restaurantData?.get("email") as? String ?: "",
                        instagram = restaurantData?.get("instagram") as? String ?: "",
                        siteWeb = restaurantData?.get("siteWeb") as? String ?: "",
                        logoUrl = restaurantData?.get("logoUrl") as? String ?: ""
                    )
                } ?: emptyList()

                restaurantList.addAll(allRestaurants)
                restaurantAdapter.notifyDataSetChanged()

            }
        // pour le moteur de recherche
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = allRestaurants.filter {
                    it.nom.contains(newText.orEmpty(), ignoreCase = true)
                }

                restaurantList.clear()
                restaurantList.addAll(filteredList)
                restaurantAdapter.notifyDataSetChanged()

                return true
            }
        })

    }
}