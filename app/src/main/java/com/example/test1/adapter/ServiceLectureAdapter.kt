package com.example.test1.adapters
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.models.ServiceLectureItem
import android.view.View
import android.view.LayoutInflater
import com.example.test1.R


class ServiceLectureAdapter(
    private val serviceList: List<ServiceLectureItem>,
    private val onItemClick: (ServiceLectureItem) -> Unit
) : RecyclerView.Adapter<ServiceLectureAdapter.ServiceLectureViewHolder>() {

    class ServiceLectureViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serviceImage: ImageView = view.findViewById(R.id.serviceImage)
        val serviceName: TextView = view.findViewById(R.id.serviceName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceLectureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_service_lecture, parent, false)
        return ServiceLectureViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceLectureViewHolder, position: Int) {
        val service = serviceList[position]
        holder.serviceName.text = service.serviceName
        holder.serviceImage.setImageResource(service.serviceImage)

        holder.itemView.setOnClickListener { onItemClick(service) }
    }

    override fun getItemCount() = serviceList.size
}