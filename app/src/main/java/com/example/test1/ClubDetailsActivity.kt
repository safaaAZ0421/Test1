package com.example.test1


import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.test1.adapter.CoachAdapter
import com.example.test1.adapter.DisciplineAdapter
import com.example.test1.adapter.PhotoPagerAdapter
import com.example.test1.adapter.PriceAdapter
import com.example.test1.adapter.ProgramAdapter
import com.example.test1.model.Club
import com.example.test1.model.Coach
import com.example.test1.model.Program
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore

class ClubDetailsActivity : AppCompatActivity() {

    private lateinit var viewPagerPhotos: ViewPager2
    private lateinit var tabLayoutPhotos: TabLayout
    private lateinit var textViewClubName: TextView
    private lateinit var textViewLocation: TextView
    private lateinit var recyclerViewDisciplines: RecyclerView
    private lateinit var recyclerViewCoaches: RecyclerView
    private lateinit var recyclerViewPrograms: RecyclerView
    private lateinit var recyclerViewPrices: RecyclerView
    private lateinit var buttonJoinClub: MaterialButton

    private lateinit var firestore: FirebaseFirestore
    private var clubId: String? = null
    private var currentClub: Club? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_details)

        clubId = intent.getStringExtra("club_id")


        initViews()
        setupToolbar()
        setupRecyclerViews()
        loadClubDetails()
    }

    private fun initViews() {
        viewPagerPhotos = findViewById(R.id.viewPagerPhotos)
        tabLayoutPhotos = findViewById(R.id.tabLayoutPhotos)
        textViewClubName = findViewById(R.id.textViewClubName)
        textViewLocation = findViewById(R.id.textViewLocation)
        recyclerViewDisciplines = findViewById(R.id.recyclerViewDisciplines)
        recyclerViewCoaches = findViewById(R.id.recyclerViewCoaches)
        recyclerViewPrograms = findViewById(R.id.recyclerViewPrograms)
        recyclerViewPrices = findViewById(R.id.recyclerViewPrices)
        buttonJoinClub = findViewById(R.id.buttonJoinClub)

        firestore = FirebaseFirestore.getInstance()
    }

   private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }




    private fun setupRecyclerViews() {
        // Configuration des RecyclerViews
        recyclerViewDisciplines.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerViewCoaches.layoutManager = LinearLayoutManager(this)
        recyclerViewPrograms.layoutManager = LinearLayoutManager(this)
        recyclerViewPrices.layoutManager = LinearLayoutManager(this)

        // Configuration du bouton d'adhésion
        buttonJoinClub.setOnClickListener {
            handleJoinClub()
        }
    }

    private fun loadClubDetails() {
        clubId?.let { id ->
            firestore.collection("clubs").document(id)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        currentClub = document.toObject(Club::class.java)
                        currentClub?.id = document.id
                        displayClubDetails()
                    } else {
                        showError("Club non trouvé")
                    }
                }
                .addOnFailureListener { exception ->
                    showError("Erreur lors du chargement: ${exception.message}")
                    Log.e("ClubDetailsActivity", "Erreur lors du chargement des information du club: ", exception)
                    Toast.makeText(this, "Erreur lors du chargement des details: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun displayClubDetails() {
        currentClub?.let { club ->
            // Affichage des informations de base
            textViewClubName.text = club.name
            textViewLocation.text = club.location

            // Configuration du ViewPager pour les photos
            if (club.photos.isNotEmpty()) {
                val photoAdapter = PhotoPagerAdapter(club.photos)
                viewPagerPhotos.adapter = photoAdapter

                TabLayoutMediator(tabLayoutPhotos, viewPagerPhotos) { _, _ ->
                    // Configuration des indicateurs de photos
                }.attach()
            }

            // Configuration des adaptateurs pour les RecyclerViews
            setupDisciplinesAdapter(club.disciplines)
            setupCoachesAdapter(club.coaches)
            setupProgramsAdapter(club.programs)
            setupPricesAdapter(club.prices)
        }
    }

   private fun setupDisciplinesAdapter(disciplines: List<String>) {
        val adapter = DisciplineAdapter(disciplines)
        recyclerViewDisciplines.adapter = adapter
    }

    private fun setupCoachesAdapter(coaches: List<Coach>) {
        val adapter = CoachAdapter(coaches)
        recyclerViewCoaches.adapter = adapter
    }

    private fun setupProgramsAdapter(programs: List<Program>) {
        val adapter = ProgramAdapter(programs)
        recyclerViewPrograms.adapter = adapter
    }

    private fun setupPricesAdapter(prices: Map<String, Double>) {
        val adapter = PriceAdapter(prices)
        recyclerViewPrices.adapter = adapter
    }

    private fun handleJoinClub() {
        // Cette méthode sera étendue pour gérer l'adhésion payante
        Toast.makeText(this, "Fonctionnalité d'adhésion à venir", Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

