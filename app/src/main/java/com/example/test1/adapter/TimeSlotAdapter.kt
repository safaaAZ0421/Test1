package com.example.test1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TimeSlot(
    val startTime: Date,
    val endTime: Date,
    val price: Double,
    val isAvailable: Boolean = true
)

class TimeSlotAdapter(
    private var timeSlots: List<TimeSlot>,
    private val onTimeSlotClick: (TimeSlot) -> Unit
) : RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {

    private var selectedPosition = -1
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    class TimeSlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView as CardView
        val textViewTimeSlot: TextView = itemView.findViewById(R.id.textViewTimeSlot)
        val textViewSlotPrice: TextView = itemView.findViewById(R.id.textViewSlotPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_slot, parent, false)
        return TimeSlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        val timeSlot = timeSlots[position]

        // Formater l'heure
        val startTimeStr = timeFormat.format(timeSlot.startTime)
        val endTimeStr = timeFormat.format(timeSlot.endTime)
        holder.textViewTimeSlot.text = "$startTimeStr - $endTimeStr"

        // Afficher le prix
        holder.textViewSlotPrice.text = "${timeSlot.price.toInt()}€"

        // Gérer l'état de sélection
        if (position == selectedPosition) {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.time_slot_selected)
            )
            holder.textViewTimeSlot.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.primary_color)
            )
        } else {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, android.R.color.white)
            )
            holder.textViewTimeSlot.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.text_primary)
            )
        }

        // Gérer le clic
        holder.itemView.setOnClickListener {
            if (timeSlot.isAvailable) {
                val previousPosition = selectedPosition
                selectedPosition = position

                // Notifier les changements pour mettre à jour l'affichage
                if (previousPosition != -1) {
                    notifyItemChanged(previousPosition)
                }
                notifyItemChanged(position)

                onTimeSlotClick(timeSlot)
            }
        }

        // Désactiver les créneaux non disponibles
        holder.itemView.isEnabled = timeSlot.isAvailable
        holder.itemView.alpha = if (timeSlot.isAvailable) 1.0f else 0.5f
    }

    override fun getItemCount(): Int = timeSlots.size

    fun updateTimeSlots(newTimeSlots: List<TimeSlot>) {
        timeSlots = newTimeSlots
        selectedPosition = -1 // Réinitialiser la sélection
        notifyDataSetChanged()
    }

    fun getSelectedTimeSlot(): TimeSlot? {
        return if (selectedPosition != -1 && selectedPosition < timeSlots.size) {
            timeSlots[selectedPosition]
        } else null
    }
}
