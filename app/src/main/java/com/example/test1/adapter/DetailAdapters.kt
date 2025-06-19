package com.example.test1.adapter



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.R
import com.example.test1.model.Coach
import com.example.test1.model.Program


class DisciplineAdapter(private val disciplines: List<String>) :
    RecyclerView.Adapter<DisciplineAdapter.DisciplineViewHolder>() {

    class DisciplineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDiscipline: TextView = itemView.findViewById(R.id.textViewDiscipline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisciplineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_discipline, parent, false)
        return DisciplineViewHolder(view)
    }

    override fun onBindViewHolder(holder: DisciplineViewHolder, position: Int) {
        holder.textViewDiscipline.text = disciplines[position]
    }

    override fun getItemCount(): Int = disciplines.size
}

class CoachAdapter(
    private val coaches: List<Coach>,
    private val onCoachClick: (Coach) -> Unit = {}
) : RecyclerView.Adapter<CoachAdapter.CoachViewHolder>() {

    class CoachViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewCoachName: TextView = itemView.findViewById(R.id.textViewCoachName)
        val textViewCoachDiscipline: TextView = itemView.findViewById(R.id.textViewCoachDiscipline)
        val textViewCoachBio: TextView = itemView.findViewById(R.id.textViewCoachBio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoachViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coach, parent, false)
        return CoachViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoachViewHolder, position: Int) {
        val coach = coaches[position]
        holder.textViewCoachName.text = coach.name
        holder.textViewCoachDiscipline.text = "Entraîneur de ${coach.discipline}"
        holder.textViewCoachBio.text = coach.bio

        // Gérer le clic sur le coach
        holder.itemView.setOnClickListener {
            onCoachClick(coach)
        }
    }

    override fun getItemCount(): Int = coaches.size
}

class ProgramAdapter(private val programs: List<Program>) :
    RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder>() {

    class ProgramViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewProgramName: TextView = itemView.findViewById(R.id.textViewProgramName)
        val textViewProgramDescription: TextView = itemView.findViewById(R.id.textViewProgramDescription)
        val textViewProgramDuration: TextView = itemView.findViewById(R.id.textViewProgramDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_program, parent, false)
        return ProgramViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProgramViewHolder, position: Int) {
        val program = programs[position]
        holder.textViewProgramName.text = program.name
        holder.textViewProgramDescription.text = program.description
        holder.textViewProgramDuration.text = "Durée: ${program.duration}"
    }

    override fun getItemCount(): Int = programs.size
}

class PriceAdapter(private val prices: Map<String, Double>) :
    RecyclerView.Adapter<PriceAdapter.PriceViewHolder>() {

    private val priceList = prices.toList()

    class PriceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPriceType: TextView = itemView.findViewById(R.id.textViewPriceType)
        val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_price, parent, false)
        return PriceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        val (type, price) = priceList[position]
        holder.textViewPriceType.text = type
        holder.textViewPrice.text = "${price.toInt()}€"
    }

    override fun getItemCount(): Int = priceList.size
}