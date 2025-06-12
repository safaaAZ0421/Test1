package com.example.test1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test1.databinding.ActivityCabinetListBinding
import com.example.test1.model.Doctor
import com.google.firebase.firestore.FirebaseFirestore

//data class Doctor(val id: String = "", val name: String = "", val specialty: String = "", val address: String = "")

class CabinetListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCabinetListBinding
    private lateinit var doctorAdapter: DoctorAdapter
    private val doctorList = mutableListOf<Doctor>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCabinetListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchDoctors()
    }

    private fun setupRecyclerView() {
        doctorAdapter = DoctorAdapter(doctorList) { doctor ->
            val intent = Intent(this, CabinetDetailsActivity::class.java)
            intent.putExtra("doctorId", doctor.id)
            intent.putExtra("doctorName", doctor.name)
            intent.putExtra("doctorSpecialty", doctor.specialty)
            intent.putExtra("doctorAddress", doctor.address)
            startActivity(intent)
        }
        binding.recyclerViewDoctors.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewDoctors.adapter = doctorAdapter
    }

    private fun fetchDoctors() {
        db.collection("doctors")
            .get()
            .addOnSuccessListener { result ->
                doctorList.clear()
                for (document in result) {
                    val doctor = document.toObject(Doctor::class.java).copy(id = document.id)
                    doctorList.add(doctor)
                }
                doctorAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("CabinetListActivity", "Error getting documents: ", exception)
                Toast.makeText(this, "Error loading doctors.", Toast.LENGTH_SHORT).show()
            }
    }
}

