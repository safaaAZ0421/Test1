package com.example.test1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.model.Doctor

class DoctorAdapter(private val doctorList: List<Doctor>, private val onItemClick: (Doctor) -> Unit) :
    RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctorList[position]
        holder.bind(doctor)
    }

    override fun getItemCount(): Int = doctorList.size

    inner class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewDoctorName)
        private val specialtyTextView: TextView = itemView.findViewById(R.id.textViewDoctorSpecialty)
        private val addressTextView: TextView = itemView.findViewById(R.id.textViewDoctorAddress)

        fun bind(doctor: Doctor) {
            nameTextView.text = doctor.name
            specialtyTextView.text = doctor.specialty
            addressTextView.text = doctor.address
            itemView.setOnClickListener { onItemClick(doctor) }
        }
    }
}

