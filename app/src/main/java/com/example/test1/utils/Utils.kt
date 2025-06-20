package com.example.test1.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun formatDate(date: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.FRENCH)
            val parsedDate = inputFormat.parse(date)
            outputFormat.format(parsedDate ?: Date())
        } catch (e: Exception) {
            date
        }
    }

    fun formatTime(time: String): String {
        return try {
            val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH'h'mm", Locale.FRENCH)
            val parsedTime = inputFormat.parse(time)
            outputFormat.format(parsedTime ?: Date())
        } catch (e: Exception) {
            time
        }
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun getCurrentTime(): String {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(Date())
    }
}