package com.example.test1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment() {

    private lateinit var editTextFirstName: TextInputEditText
    private lateinit var editTextLastName: TextInputEditText
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var editTextConfirmPassword: TextInputEditText
    private lateinit var checkBoxTerms: CheckBox
    private lateinit var buttonRegister: MaterialButton
    private lateinit var buttonGoogleRegister: MaterialButton

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var authListener: LoginActivity.AuthListener? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupAuth()
        setupClickListeners()
    }

    private fun initViews(view: View) {
        editTextFirstName = view.findViewById(R.id.editTextFirstName)
        editTextLastName = view.findViewById(R.id.editTextLastName)
        editTextEmail = view.findViewById(R.id.editTextEmail)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPassword)
        checkBoxTerms = view.findViewById(R.id.checkBoxTerms)
        buttonRegister = view.findViewById(R.id.buttonRegister)
        buttonGoogleRegister = view.findViewById(R.id.buttonGoogleRegister)
    }

    private fun setupAuth() {
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Vérifier si l'activité parent implémente AuthListener
        if (activity is LoginActivity.AuthListener) {
            authListener = activity as LoginActivity.AuthListener
        }
    }

    private fun setupClickListeners() {
        buttonRegister.setOnClickListener {
            performRegistration()
        }

        buttonGoogleRegister.setOnClickListener {
            // TODO: Implémenter l'inscription Google
            Toast.makeText(context, "Inscription Google à venir", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performRegistration() {
        val firstName = editTextFirstName.text.toString().trim()
        val lastName = editTextLastName.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        val confirmPassword = editTextConfirmPassword.text.toString().trim()

        if (!validateInput(firstName, lastName, email, password, confirmPassword)) {
            return
        }

        authListener?.showLoading(true)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        updateUserProfile(it, firstName, lastName)
                        createUserDocument(it, firstName, lastName)
                    }
                } else {
                    authListener?.showLoading(false)
                    val errorMessage = task.exception?.message ?: "Erreur lors de l'inscription"
                    authListener?.onAuthError(errorMessage)
                }
            }
    }

    private fun validateInput(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (firstName.isEmpty()) {
            editTextFirstName.error = "Le prénom est requis"
            editTextFirstName.requestFocus()
            return false
        }

        if (lastName.isEmpty()) {
            editTextLastName.error = "Le nom est requis"
            editTextLastName.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            editTextEmail.error = "L'adresse e-mail est requise"
            editTextEmail.requestFocus()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Adresse e-mail invalide"
            editTextEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            editTextPassword.error = "Le mot de passe est requis"
            editTextPassword.requestFocus()
            return false
        }

        if (password.length < 6) {
            editTextPassword.error = "Le mot de passe doit contenir au moins 6 caractères"
            editTextPassword.requestFocus()
            return false
        }

        if (confirmPassword != password) {
            editTextConfirmPassword.error = "Les mots de passe ne correspondent pas"
            editTextConfirmPassword.requestFocus()
            return false
        }

        if (!checkBoxTerms.isChecked) {
            Toast.makeText(context, "Vous devez accepter les conditions d'utilisation", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun updateUserProfile(user: FirebaseUser, firstName: String, lastName: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName("$firstName $lastName")
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Profil mis à jour avec succès
                }
            }
    }

    private fun createUserDocument(user: FirebaseUser, firstName: String, lastName: String) {
        val userDocument = hashMapOf(
            "uid" to user.uid,
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to user.email,
            "createdAt" to com.google.firebase.Timestamp.now(),
            "isActive" to true,
            "isMember" to false // Initialiser le statut d'adhésion à false
        )

        firestore.collection("Users").document(user.uid)
            .set(userDocument)
            .addOnCompleteListener { task ->
                authListener?.showLoading(false)

                if (task.isSuccessful) {
                    authListener?.onAuthSuccess(user)
                } else {
                    // Document non créé mais utilisateur créé
                    authListener?.onAuthSuccess(user)
                }
            }
    }
}

