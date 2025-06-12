package com.example.test1

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.test1.databinding.ActivityCabinetDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CabinetDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCabinetDetailsBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var selectedDate: Calendar? = null
    private var selectedTime: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCabinetDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val doctorId = intent.getStringExtra("doctorId")
        val doctorName = intent.getStringExtra("doctorName")
        val doctorSpecialty = intent.getStringExtra("doctorSpecialty")
        val doctorAddress = intent.getStringExtra("doctorAddress")

        binding.textViewDoctorName.text = doctorName
        binding.textViewDoctorSpecialty.text = doctorSpecialty
        binding.textViewDoctorAddress.text = doctorAddress

        binding.buttonSelectDate.setOnClickListener { showDatePicker() }
        binding.buttonSelectTime.setOnClickListener { showTimePicker() }
        binding.buttonBookAppointment.setOnClickListener { bookAppointment(doctorId, doctorName) }
        binding.buttonCancelAppointment.setOnClickListener { cancelAppointment(doctorId) }

        checkExistingReservation(doctorId)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, {
                _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.textViewSelectedDate.text = dateFormat.format(selectedDate!!.time)
        }, year, month, day)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000 // Prevent selecting past dates
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, {
                _, selectedHour, selectedMinute ->
            selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, selectedHour)
                set(Calendar.MINUTE, selectedMinute)
            }
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            binding.textViewSelectedTime.text = timeFormat.format(selectedTime!!.time)
        }, hour, minute, true)
        timePickerDialog.show()
    }

    private fun bookAppointment(doctorId: String?, doctorName: String?) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            return
        }

        if (doctorId == null || doctorName == null || selectedDate == null || selectedTime == null) {
            Toast.makeText(this, "Please select a date and time.", Toast.LENGTH_SHORT).show()
            return
        }

        val appointmentDateTime = Calendar.getInstance().apply {
            set(selectedDate!!.get(Calendar.YEAR),
                selectedDate!!.get(Calendar.MONTH),
                selectedDate!!.get(Calendar.DAY_OF_MONTH),
                selectedTime!!.get(Calendar.HOUR_OF_DAY),
                selectedTime!!.get(Calendar.MINUTE))
        }

        val appointment = hashMapOf(
            "userId" to userId,
            "doctorId" to doctorId,
            "doctorName" to doctorName,
            "timestamp" to appointmentDateTime.time
        )

        db.collection("appointments")
            .add(appointment)
            .addOnSuccessListener {
                Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_LONG).show()
                showConfirmationDialog("Your appointment with Dr. $doctorName on ${binding.textViewSelectedDate.text} at ${binding.textViewSelectedTime.text} is confirmed.")
                updateUIForBookedAppointment()
            }
            .addOnFailureListener { e ->
                Log.w("CabinetDetailsActivity", "Error adding document", e)
                Toast.makeText(this, "Error booking appointment.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cancelAppointment(doctorId: String?) {
        val userId = auth.currentUser?.uid
        if (userId == null || doctorId == null) {
            Toast.makeText(this, "Cannot cancel: User not logged in or doctor ID missing.", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("appointments")
            .whereEqualTo("userId", userId)
            .whereEqualTo("doctorId", doctorId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        db.collection("appointments").document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                Toast.makeText(this, "Appointment cancelled successfully.", Toast.LENGTH_SHORT).show()
                                updateUIForCancelledAppointment()
                            }
                            .addOnFailureListener { e ->
                                Log.w("CabinetDetailsActivity", "Error deleting document", e)
                                Toast.makeText(this, "Error cancelling appointment.", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "No existing appointment to cancel.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.w("CabinetDetailsActivity", "Error checking for existing appointment", e)
                Toast.makeText(this, "Error checking for existing appointment.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkExistingReservation(doctorId: String?) {
        val userId = auth.currentUser?.uid
        if (userId == null || doctorId == null) {
            return
        }

        db.collection("appointments")
            .whereEqualTo("userId", userId)
            .whereEqualTo("doctorId", doctorId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val appointment = documents.documents[0].data
                    val timestamp = appointment?.get("timestamp") as? com.google.firebase.Timestamp
                    if (timestamp != null) {
                        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(timestamp.toDate())
                        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(timestamp.toDate())
                        binding.textViewExistingAppointment.text = "Existing appointment: $date at $time"
                        updateUIForBookedAppointment()
                    }
                } else {
                    updateUIForCancelledAppointment()
                }
            }
            .addOnFailureListener { e ->
                Log.w("CabinetDetailsActivity", "Error checking existing reservation", e)
            }
    }

    private fun updateUIForBookedAppointment() {
        binding.buttonBookAppointment.isEnabled = false
        binding.buttonSelectDate.isEnabled = false
        binding.buttonSelectTime.isEnabled = false
        binding.buttonCancelAppointment.isEnabled = true
    }

    private fun updateUIForCancelledAppointment() {
        binding.buttonBookAppointment.isEnabled = true
        binding.buttonSelectDate.isEnabled = true
        binding.buttonSelectTime.isEnabled = true
        binding.buttonCancelAppointment.isEnabled = false
        binding.textViewExistingAppointment.text = "No existing appointment."
        binding.textViewSelectedDate.text = ""
        binding.textViewSelectedTime.text = ""
        selectedDate = null
        selectedTime = null
    }

    private fun showConfirmationDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Appointment Confirmation")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}

