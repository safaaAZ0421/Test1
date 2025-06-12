package com.example.test1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AjouterImageRestaurantActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var uploadButton: Button
    private var selectedImageUri: Uri? = null

    // Lancer la galerie et récupérer l'image
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            imageView.setImageURI(selectedImageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajouter_image_restaurant)

        imageView = findViewById(R.id.imagePreview)
        uploadButton = findViewById(R.id.btnUpload)

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }

        uploadButton.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener
            val restaurantId = intent.getStringExtra("restaurantId") ?: return@setOnClickListener

            if (selectedImageUri != null) {
                val ref = FirebaseStorage.getInstance().reference
                    .child("restaurant_images/${UUID.randomUUID()}.jpg")

                ref.putFile(selectedImageUri!!)
                    .addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener { uri ->
                            FirebaseFirestore.getInstance().collection("Restaurants")
                                .document(restaurantId)
                                .update("imageUrl", uri.toString())
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Image enregistrée", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erreur upload : ${it.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Sélectionnez une image d'abord", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
