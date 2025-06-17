package com.example.test1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.R
import com.example.test1.models.ServiceHomeItem

class ServiceHomeAdapter(
    private val serviceList: List<ServiceHomeItem>,
    private val onItemClick: (ServiceHomeItem) -> Unit
) : RecyclerView.Adapter<ServiceHomeAdapter.ServiceHomeViewHolder>() {

    class ServiceHomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serviceImage: ImageView = view.findViewById(R.id.serviceImage)
        val serviceName: TextView = view.findViewById(R.id.serviceName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceHomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_service_home, parent, false)
        return ServiceHomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceHomeViewHolder, position: Int) {
        val service = serviceList[position]
        holder.serviceName.text = service.serviceName
        holder.serviceImage.setImageResource(service.serviceImage)

        holder.itemView.setOnClickListener { onItemClick(service) }
    }

    override fun getItemCount() = serviceList.size
}