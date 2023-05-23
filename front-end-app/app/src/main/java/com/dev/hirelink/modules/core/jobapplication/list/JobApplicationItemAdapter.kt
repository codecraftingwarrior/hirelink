package com.dev.hirelink.modules.core.jobapplication.list

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dev.hirelink.R
import com.dev.hirelink.enums.JobApplicationState
import com.dev.hirelink.models.JobApplication

class JobApplicationItemAdapter(
    val context: Context,
    val dataset: MutableList<JobApplication?>?
) : RecyclerView.Adapter<JobApplicationItemAdapter.JobApplicationItemViewHolder>() {

    class JobApplicationItemViewHolder(private val view: View) : ViewHolder(view) {
        private val logo: ImageView = view.findViewById(R.id.offer_img_view_company_logo)
        private val companyName: TextView = view.findViewById(R.id.company_name)
        private val jobTitle: TextView = view.findViewById(R.id.text_view_job_title)
        private val salaryRange: TextView = view.findViewById(R.id.text_view_salary_range)
        private val location: TextView = view.findViewById(R.id.text_view_location)
        private val timeagoText: TextView = view.findViewById(R.id.text_view_timeago)
        private val stateIndicator: View = view.findViewById(R.id.state_indicator)
        private val stateText: TextView = view.findViewById(R.id.text_view_state_text)

        fun bind(jobApplication: JobApplication) {
            companyName.text = jobApplication.jobOffer?.companyName
            jobTitle.text = jobApplication.jobOffer?.title
            salaryRange.text = view.context.getString(
                R.string.salary_range_str,
                jobApplication.jobOffer?.maxSalary!! - 10000.0f, jobApplication.jobOffer.maxSalary
            )
            location.text = jobApplication.jobOffer.address
            stateText.text = view.context.getString(JobApplicationState.valueOf(jobApplication.state!!).stringResourceId)

            stateText.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    JobApplicationState.valueOf(jobApplication.state!!).colorResourceId
                )
            )

            val stateIndicatorBg = stateIndicator.background as GradientDrawable
            stateIndicatorBg.setColor(
                ContextCompat.getColor(
                    itemView.context,
                    JobApplicationState.valueOf(jobApplication.state!!).colorResourceId
                )
            )

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JobApplicationItemViewHolder {
        val adapterLayout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_job_application_list_item, parent, false)

        return JobApplicationItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int = dataset?.size!!

    override fun onBindViewHolder(holder: JobApplicationItemViewHolder, position: Int) {
        dataset?.get(position)?.let { holder.bind(it) }
    }
}