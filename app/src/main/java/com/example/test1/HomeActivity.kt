package com.example.test1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnSante = findViewById<Button>(R.id.btnSante)
        btnSante.setOnClickListener {
            val intent = Intent(this, CabinetListActivity::class.java)
            startActivity(intent)
        }

        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        welcomeText.text = "Bienvenue sur l'application !"
    }
}

