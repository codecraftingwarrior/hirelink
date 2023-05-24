package com.dev.hirelink.modules.core.notification

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dev.hirelink.R
import com.dev.hirelink.enums.JobApplicationState
import com.dev.hirelink.models.Notification
import com.dev.hirelink.modules.core.offers.list.JobOfferItemAdapter

class NotificationItemAdapter(
    val context: Context,
    var dataset: MutableList<Notification?>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    class NotificationItemViewHolder(private val view: View) : ViewHolder(view) {
        private val initials: TextView = view.findViewById(R.id.logo_inside_circle)
        private val companyName: TextView = view.findViewById(R.id.notification_company_name)
        private val title: TextView = view.findViewById(R.id.candidacy_title)
        private val jobTitle: TextView = view.findViewById(R.id.first_job_text)
        private val stateIndicator: View = view.findViewById(R.id.state_indicator_notification)

        fun bind(notification: Notification) {
            initials.text =
                notification.jobApplication?.jobOffer?.owner?.company?.name?.get(0).toString()
            title.text = notification.title
            jobTitle.text = itemView.context.getString(
                R.string.job_title_with_point,
                notification.jobApplication?.jobOffer?.title
            )
            companyName.text = notification.jobApplication?.jobOffer?.owner?.company?.name
            val state =
                if (notification.title?.contains("rejected") == true) JobApplicationState.REFUSED else if (notification.title?.contains(
                        "accepted"
                    ) == true
                ) JobApplicationState.ACCEPTED else JobApplicationState.IN_PROGRESS

            val stateIndicatorBg = stateIndicator.background as GradientDrawable
            stateIndicatorBg.setColor(
                ContextCompat.getColor(
                    itemView.context,
                    state.colorResourceId
                )
            )

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val adapterLayout = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.row_notification_list_item, parent, false)
            return NotificationItemViewHolder(adapterLayout)
        }
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.loading_item_row, parent, false)

        return JobOfferItemAdapter.LoadingViewHolder(view)

    }

    override fun getItemCount(): Int = dataset?.size!!

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NotificationItemViewHolder) {
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

    private fun bindListeners(view: View, notification: Notification) {
        /*view.findViewById<ConstraintLayout>(R.id.schedule_card)?.setOnClickListener {
            (JobOfferBottomSheetDetailFragment(jobApplication.jobOffer!!, false)).show(
                (context as BaseActivity).supportFragmentManager,
                JobOfferBottomSheetDetailFragment.TAG
            )
        }*/
    }
}