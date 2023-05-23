package com.dev.hirelink.modules.core.employer.candidacy.list

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dev.hirelink.R
import com.dev.hirelink.components.toTimeAgo
import com.dev.hirelink.models.JobApplication
import com.dev.hirelink.modules.core.employer.candidacy.detail.CandidacyDetailActivity
import com.dev.hirelink.modules.core.offers.list.JobOfferItemAdapter
import io.reactivex.disposables.CompositeDisposable

class CandidacyItemAdapter(
    val context: Context,
    var dataset: MutableList<JobApplication?>?
) : RecyclerView.Adapter<ViewHolder>() {
    private val compositeDisposable = CompositeDisposable()
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    class CandidacyItemViewHolder(private val view: View) : ViewHolder(view) {
        private val applicantInitials: TextView =
            view.findViewById(R.id.text_view_candidate_initials)
        private val applicantName: TextView = view.findViewById(R.id.text_view_candidate_name)
        private val timeago: TextView = view.findViewById(R.id.text_view_candidate_timeago)

        fun bind(jobApplication: JobApplication) {
            applicantInitials.text = listOfNotNull(
                jobApplication.applicant?.firstName?.get(0),
                jobApplication.applicant?.lastName?.get(0)
            ).joinToString("")

            applicantName.text = listOfNotNull(
                jobApplication.applicant?.firstName,
                jobApplication.applicant?.lastName
            ).joinToString(" ")

            timeago.text = toTimeAgo(jobApplication.createdAt!!)
        }
    }

    override fun getItemCount(): Int = dataset?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        return if (dataset?.get(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val adapterLayout = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.row_candidacy_list_item, parent, false)
            return CandidacyItemViewHolder(adapterLayout)
        }
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.loading_item_row, parent, false)

        return JobOfferItemAdapter.LoadingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is CandidacyItemViewHolder) {

            holder.bind(dataset?.get(position)!!)
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

    private fun bindListeners(itemView: View, jobApplication: JobApplication) {
        itemView.findViewById<ConstraintLayout>(R.id.root_constraint_layout_candidacy_list_item)
            .setOnClickListener {
                (context as CandidacyListActivity).startActivity(
                    Intent(context, CandidacyDetailActivity::class.java).apply {
                        putExtra("jobApplicationID", jobApplication.id)
                    }
                )
            }
    }

}