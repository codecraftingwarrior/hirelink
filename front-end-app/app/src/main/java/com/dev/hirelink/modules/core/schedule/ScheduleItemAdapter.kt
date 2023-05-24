package com.dev.hirelink.modules.core.schedule

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dev.hirelink.R
import com.dev.hirelink.components.dateAsString
import com.dev.hirelink.components.formatDateFromNow
import com.dev.hirelink.enums.JobApplicationState
import com.dev.hirelink.models.JobApplication
import com.dev.hirelink.modules.core.BaseActivity
import com.dev.hirelink.modules.core.jobapplication.list.JobApplicationItemAdapter
import com.dev.hirelink.modules.core.offers.detail.JobOfferBottomSheetDetailFragment
import com.dev.hirelink.modules.core.offers.list.JobOfferItemAdapter
import java.time.LocalDate

class ScheduleItemAdapter(
    val context: Context,
    var dataset: MutableList<JobApplication?>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    class ScheduleItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val initials: TextView = view.findViewById(R.id.text_view_company_initials)
        private val date: TextView = view.findViewById(R.id.schedule_date)
        private val companyName: TextView = view.findViewById(R.id.schedule_company_name)
        private val jobTitle: TextView = view.findViewById(R.id.text_view_schedule_job_title)
        private val location: TextView = view.findViewById(R.id.text_view__schedule_location)
        private val futureDate: TextView = view.findViewById(R.id.schedule_future_date)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(jobApplication: JobApplication) {
            initials.text = jobApplication.jobOffer?.owner?.company?.name?.get(0).toString()
            companyName.text = jobApplication.jobOffer?.owner?.company?.name
            jobTitle.text = jobApplication.jobOffer?.title
            date.text = dateAsString(jobApplication.jobOffer?.toDate!!, "d MMMM yyyy")
            location.text = jobApplication.jobOffer.address

            val fromNow = LocalDate.parse(dateAsString(jobApplication.jobOffer.toDate!!, "yyyy-MM-dd"))
            val formattedText = formatDateFromNow(fromNow)
            futureDate.text = formattedText
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val adapterLayout = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.row_schedule_list_item, parent, false)
            return ScheduleItemViewHolder(adapterLayout)
        }
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.loading_item_row, parent, false)

        return JobOfferItemAdapter.LoadingViewHolder(view)

    }

    override fun getItemCount(): Int = dataset?.size!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ScheduleItemViewHolder) {
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
        view.findViewById<ConstraintLayout>(R.id.schedule_card)?.setOnClickListener {
            (JobOfferBottomSheetDetailFragment(jobApplication.jobOffer!!, false)).show(
                (context as BaseActivity).supportFragmentManager,
                JobOfferBottomSheetDetailFragment.TAG
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataset?.get(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }
}