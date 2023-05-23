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
import com.dev.hirelink.modules.core.offers.list.JobOfferItemAdapter

class JobApplicationItemAdapter(
    val context: Context,
    var dataset: MutableList<JobApplication?>?
) : RecyclerView.Adapter<ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    class JobApplicationItemViewHolder(private val view: View) : ViewHolder(view) {
        private val initials: TextView = view.findViewById(R.id.text_view_offer_ja_initials)
        private val companyName: TextView = view.findViewById(R.id.company_name)
        private val jobTitle: TextView = view.findViewById(R.id.text_view_job_title)
        private val salaryRange: TextView = view.findViewById(R.id.text_view_salary_range)
        private val location: TextView = view.findViewById(R.id.text_view_location)
        private val timeagoText: TextView = view.findViewById(R.id.text_view_timeago)
        private val stateIndicator: View = view.findViewById(R.id.state_indicator)
        private val stateText: TextView = view.findViewById(R.id.text_view_state_text)

        fun bind(jobApplication: JobApplication) {
            initials.text = jobApplication.jobOffer?.owner?.company?.name?.get(0).toString()
            companyName.text = jobApplication.jobOffer?.owner?.company?.name
            jobTitle.text = jobApplication.jobOffer?.title
            salaryRange.text = view.context.getString(
                R.string.salary_range_str,
                jobApplication.jobOffer?.maxSalary!!, jobApplication.jobOffer.maxSalary
            )
            location.text = jobApplication.jobOffer.address
            stateText.text =
                view.context.getString(JobApplicationState.valueOf(jobApplication.state!!).stringResourceId)

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
    ): ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val adapterLayout = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.row_job_application_list_item, parent, false)
            return JobApplicationItemViewHolder(adapterLayout)
        }
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.loading_item_row, parent, false)

        return JobOfferItemAdapter.LoadingViewHolder(view)

    }

    override fun getItemCount(): Int = dataset?.size!!

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is JobApplicationItemViewHolder) {
            dataset?.get(position)?.let { holder.bind(it) }
            bindListeners(holder.itemView, dataset?.get(position)!!)
        } else if (holder is JobOfferItemAdapter.LoadingViewHolder) {
            (holder as JobOfferItemAdapter.LoadingViewHolder?)?.let {
                showLoadingView(
                    it,
                    position
                )
            }
        }
    }

    private fun showLoadingView(viewHolder: JobOfferItemAdapter.LoadingViewHolder, position: Int) {
        //ProgressBar would be displayed
    }

    private fun bindListeners(view: View, jobApplication: JobApplication) {

    }

    override fun getItemViewType(position: Int): Int {
        return if (dataset?.get(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }
}