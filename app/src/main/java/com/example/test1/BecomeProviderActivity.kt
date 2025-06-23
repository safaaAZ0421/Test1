package com.example.test1

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class BecomeProviderActivity : AppCompatActivity() {
    private lateinit var serviceSpinner: Spinner
    private lateinit var confirmButton: Button

    // Champs pour le service Restauration
    private lateinit var restoLayout: LinearLayout
    private lateinit var restoNameInput: EditText
    private lateinit var restoAdresseInput: EditText
    private lateinit var restoTelInput: EditText
    private lateinit var restoEmailInput: EditText
    private lateinit var restoFacebookInput: EditText
    private lateinit var restoInstagramInput: EditText
    private lateinit var restoSiteWebInput: EditText

    // CheckBoxes pour jours d'ouverture
    private lateinit var checkboxLundi: CheckBox
    private lateinit var checkboxMardi: CheckBox
    private lateinit var checkboxMercredi: CheckBox
    private lateinit var checkboxJeudi: CheckBox
    private lateinit var checkboxVendredi: CheckBox
    private lateinit var checkboxSamedi: CheckBox
    private lateinit var checkboxDimanche: CheckBox

    // Horaires d'ouverture et de fermeture
    private lateinit var heureDebutInput: EditText
    private lateinit var heureFinInput: EditText

    // Image sélectionnée
    private lateinit var selectedImageView: ImageView
    private var selectedImageUri: Uri? = null
    /*private lateinit var btnSelectImage: Button


    // Lancer la galerie avec résultat
    private val selectImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            selectedImageView.setImageURI(selectedImageUri)
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_become_provider)

        serviceSpinner = findViewById(R.id.serviceSpinner)
        confirmButton = findViewById(R.id.butSuivant)

        // Initialisation des champs de formulaire pour le service restauration
        restoLayout = findViewById(R.id.restoLayout)
        restoNameInput = findViewById(R.id.restoNameInput)
        restoAdresseInput = findViewById(R.id.restoAdresseInput)
        restoTelInput = findViewById(R.id.restoTelInput)
        restoEmailInput = findViewById(R.id.restoEmailInput)
        restoFacebookInput = findViewById(R.id.restoFacebookInput)
        restoInstagramInput = findViewById(R.id.restoInstagramInput)
        restoSiteWebInput = findViewById(R.id.restoSiteWebInput)

        checkboxLundi = findViewById(R.id.checkboxLundi)
        checkboxMardi = findViewById(R.id.checkboxMardi)
        checkboxMercredi = findViewById(R.id.checkboxMercredi)
        checkboxJeudi = findViewById(R.id.checkboxJeudi)
        checkboxVendredi = findViewById(R.id.checkboxVendredi)
        checkboxSamedi = findViewById(R.id.checkboxSamedi)
        checkboxDimanche = findViewById(R.id.checkboxDimanche)

        heureDebutInput = findViewById(R.id.heureDebutInput)
        heureFinInput = findViewById(R.id.heureFinInput)

        /*btnSelectImage = findViewById(R.id.btnSelectImage)
        selectedImageView = findViewById(R.id.selectedImageView)*/

        // TimePickerDialog pour heure d'ouverture
        val timeSetListenerDebut = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            val heure = String.format("%02d:%02d", hour, minute)
            heureDebutInput.setText(heure)
        }
        heureDebutInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(this, timeSetListenerDebut,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show()
        }

        // TimePickerDialog pour heure de fermeture
        val timeSetListenerFin = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            val heure = String.format("%02d:%02d", hour, minute)
            heureFinInput.setText(heure)
        }
        heureFinInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(this, timeSetListenerFin,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show()
        }

        // Liste des services disponibles
        val services = arrayOf("Salle de sport", "Bibliothèque", "Ménage", "Santé", "Restauration", "Beauté")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, services)
        serviceSpinner.adapter = adapter

        // Afficher le formulaire restauration seulement si "Restauration" est sélectionné
        serviceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = services[position]
                restoLayout.visibility = if (selected == "Restauration") View.VISIBLE else View.GONE
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Bouton pour sélectionner une image
        /*btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            selectImageLauncher.launch(intent)
        }*/

        // Bouton de confirmation pour enregistrer le prestataire et les données
        confirmButton.setOnClickListener {
            val selectedService = serviceSpinner.selectedItem.toString()
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener

            // Mise à jour du rôle de l'utilisateur
            val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)
            userRef.update(mapOf(
                "role" to "prestataire",
                "service" to selectedService
            ))

            if (selectedService == "Restauration") {
                // Construction des horaires
                val horaires = mutableListOf<String>()
                val heureDebut = heureDebutInput.text.toString()
                val heureFin = heureFinInput.text.toString()

                fun jour(j: CheckBox, nom: String) {
                    if (j.isChecked) {
                        horaires.add("$nom $heureDebut–$heureFin")
                    } else {
                        horaires.add("$nom fermé")
                    }
                }

                jour(checkboxLundi, "lundi")
                jour(checkboxMardi, "mardi")
                jour(checkboxMercredi, "mercredi")
                jour(checkboxJeudi, "jeudi")
                jour(checkboxVendredi, "vendredi")
                jour(checkboxSamedi, "samedi")
                jour(checkboxDimanche, "dimanche")

                // Envoi des données vers la prochaine activité pour gestion de l'image
                val intent = Intent(this, AjouterImageRestaurantActivity::class.java)
                intent.putExtra("nom", restoNameInput.text.toString())
                intent.putExtra("adresse", restoAdresseInput.text.toString())
                intent.putExtra("telephone", restoTelInput.text.toString())
                intent.putExtra("email", restoEmailInput.text.toString())
                intent.putExtra("facebook", restoFacebookInput.text.toString())
                intent.putExtra("instagram", restoInstagramInput.text.toString())
                intent.putExtra("siteWeb", restoSiteWebInput.text.toString())
                intent.putExtra("prestataireId", userId)
                intent.putExtra("type", "Restauration")
                intent.putExtra("note", 4.0)
                intent.putStringArrayListExtra("horaires", ArrayList(horaires))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Service sauvegardé sans données spécifiques", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }
}
