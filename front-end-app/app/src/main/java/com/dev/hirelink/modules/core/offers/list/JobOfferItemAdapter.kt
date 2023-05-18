package com.dev.hirelink.modules.core.offers.list

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dev.hirelink.R
import com.dev.hirelink.components.toTimeAgo
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.models.JobOffer
import com.dev.hirelink.modules.core.BaseActivity
import com.dev.hirelink.modules.core.jobapplication.create.JobApplicationCreateFragment


class JobOfferItemAdapter(
    val context: Context,
    var dataset: MutableList<JobOffer?>?
) : Adapter<ViewHolder>() {
    private lateinit var currentUser: ApplicationUser
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        currentUser = (context as BaseActivity).currentUser
    }

    class JobOfferItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val logo: ImageView =
            view.findViewById(com.dev.hirelink.R.id.offer_img_view_company_logo)
        private val companyName: TextView = view.findViewById(com.dev.hirelink.R.id.company_name)
        private val jobTitle: TextView =
            view.findViewById(com.dev.hirelink.R.id.text_view_job_title)
        private val salaryRange: TextView =
            view.findViewById(com.dev.hirelink.R.id.text_view_salary_range)
        private val location: TextView = view.findViewById(com.dev.hirelink.R.id.text_view_location)
        private val timeagoText: TextView =
            view.findViewById(com.dev.hirelink.R.id.text_view_timeago)
        val buttonApply: Button = view.findViewById(R.id.button_apply_offer)

        fun bind(jobOffer: JobOffer) {
            companyName.text = jobOffer.owner?.company?.name ?: "unknown"
            jobTitle.text = jobOffer.title
            salaryRange.text = view.context.getString(
                com.dev.hirelink.R.string.salary_range_str,
                jobOffer.minSalary,
                jobOffer.maxSalary
            )
            location.text = jobOffer.address
            timeagoText.text = toTimeAgo(jobOffer.createdAt ?: "2023-05-02T13:50:37+00:00")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val adapterLayout = LayoutInflater
                .from(parent.context)
                .inflate(com.dev.hirelink.R.layout.row_offer_list_item, parent, false)
            return JobOfferItemViewHolder(adapterLayout)
        }
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.loading_item_row, parent, false)

        return LoadingViewHolder(view)
    }

    private fun bindListeners(view: View, jobOffer: JobOffer) {
        view.findViewById<AppCompatButton>(R.id.button_apply_offer)?.setOnClickListener {
            (JobApplicationCreateFragment(jobOffer)).show(
                (context as BaseActivity).supportFragmentManager,
                JobApplicationCreateFragment.TAG
            )
        }
    }

    override fun getItemCount(): Int = dataset?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        return if (dataset?.get(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is JobOfferItemViewHolder) {
            holder.bind(dataset?.get(position)!!)
            bindListeners(holder.itemView, dataset?.get(position)!!)
        } else if (holder is LoadingViewHolder) {
            (holder as LoadingViewHolder?)?.let { showLoadingView(it, position) }
        }

    }

    private class LoadingViewHolder(itemView: View) : ViewHolder(itemView) {
        var progressBar: ProgressBar

        init {
            progressBar =
                itemView.findViewById(com.dev.hirelink.R.id.item_circular_progress_indicator)
        }
    }

    private fun showLoadingView(viewHolder: LoadingViewHolder, position: Int) {
        //ProgressBar would be displayed
    }
}