package com.dev.hirelink.modules.core.offers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.dev.hirelink.R
import com.dev.hirelink.models.JobOffer
import com.dev.hirelink.modules.auth.register.adapters.PlanItemAdapter

class JobOfferItemAdapter(
    val context: Context,
    val dataset: List<JobOffer>
) : Adapter<JobOfferItemAdapter.JobOfferItemViewHolder>() {

    class JobOfferItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val logo: ImageView = view.findViewById(R.id.offer_img_view_company_logo)
        private val companyName: TextView = view.findViewById(R.id.company_name)
        private val jobTitle: TextView = view.findViewById(R.id.text_view_job_title)
        private val salaryRange: TextView = view.findViewById(R.id.text_view_salary_range)
        private val location: TextView = view.findViewById(R.id.text_view_location)
        private val timeagoText: TextView = view.findViewById(R.id.text_view_timeago)

        fun bind(jobOffer: JobOffer) {
            companyName.text = jobOffer.companyName
            jobTitle.text = jobOffer.title
            salaryRange.text = jobOffer.salary.toString()
            location.text = jobOffer.address
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobOfferItemViewHolder {
        val adapterLayout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_offer_list_item, parent, false)

        return JobOfferItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: JobOfferItemViewHolder, position: Int) {
        holder.bind(dataset[position])
    }
}