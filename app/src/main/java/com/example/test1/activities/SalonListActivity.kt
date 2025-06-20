package com.example.test1.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.R
import com.example.test1.adapter.SalonAdapter
import com.example.test1.model.Salon
import com.example.test1.services.SalonService

class SalonListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var salonAdapter: SalonAdapter
    private val salonList = mutableListOf<Salon>()
    private val salonService = SalonService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salon_list)

        setupRecyclerView()
        loadSalons()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewSalons)
        salonAdapter = SalonAdapter(salonList) { salon ->
            val intent = Intent(this, SalonDetailActivity::class.java)
            intent.putExtra("salon_id", salon.id)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = salonAdapter
    }

    private fun loadSalons() {
        salonService.getAllSalons(
            callback = { salons ->
                salonList.clear()
                salonList.addAll(salons)
                salonAdapter.notifyDataSetChanged()
            },
            onError = { exception ->
                // Gérer l'erreur
            }
        )
    }
}