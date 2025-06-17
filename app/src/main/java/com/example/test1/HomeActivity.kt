// 📁 HomeActivity.kt
package com.example.test1
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.models.ServiceHomeItem
import com.example.test1.adapters.ServiceHomeAdapter
import com.example.test1.activities.ServiceLectureActivity
import android.widget.Toast

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var serviceAdapter: ServiceHomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // Affichage en grille (2 colonnes)

        val serviceList = listOf(
            ServiceHomeItem("Lecture", R.drawable.ic_book, ServiceLectureActivity::class.java)
        )

        serviceAdapter = ServiceHomeAdapter(serviceList) { service ->
            val intent = Intent(this, service.activityClass)
            startActivity(intent)
        }

        recyclerView.adapter = serviceAdapter
    }
}