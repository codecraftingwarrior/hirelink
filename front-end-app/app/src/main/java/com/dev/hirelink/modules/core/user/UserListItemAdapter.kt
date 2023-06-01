package com.dev.hirelink.modules.core.user

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dev.hirelink.R
import com.dev.hirelink.enums.JobApplicationState
import com.dev.hirelink.enums.RoleType
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.models.Role
import com.dev.hirelink.modules.core.offers.list.JobOfferItemAdapter
import io.reactivex.disposables.CompositeDisposable

class UserListItemAdapter(
    val context: Context,
    var dataset: MutableList<ApplicationUser?>?
) : Adapter<ViewHolder>() {

    private val compositeDisposable = CompositeDisposable()
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    class UserItemViewHolder(private val view: View): ViewHolder(view) {
        private val initials: TextView = view.findViewById(R.id.gestionnaire_user_logo_company)
        private val name: TextView = view.findViewById(R.id.gestionnaire_user_text_view_company_name)
        private val ceoName: TextView = view.findViewById(R.id.gestionnaire_user_text_view_ceo)
        private val stateText: TextView = view.findViewById(R.id.gestionnaire_user_text_view_refused)
        private val stateIndicator: View = view.findViewById(R.id.gestionnaire_user_red_point)

        fun bind(user: ApplicationUser) {
            initials.text = when(user.role?.code) {
                RoleType.APPLICANT.code -> listOfNotNull(user.firstName?.get(0), user.lastName?.get(0)).joinToString("")
                RoleType.INTERIM_AGENCY.code, RoleType.EMPLOYER.code -> user.company?.name?.get(0).toString()
                else -> "unknown"
            }

            name.text = when(user.role?.code) {
                RoleType.APPLICANT.code -> user.lastName
                RoleType.INTERIM_AGENCY.code, RoleType.EMPLOYER.code -> user.company?.name
                else -> "untouch"
            }

            ceoName.text = when(user.role?.code) {
                RoleType.INTERIM_AGENCY.code, RoleType.EMPLOYER.code -> listOfNotNull(user.firstName, user.lastName).joinToString(" ")
                RoleType.APPLICANT.code -> user.firstName
                else -> "untouch"
            }

            stateText.text =
                view.context.getString(JobApplicationState.valueOf("PENDING").stringResourceId)

            stateText.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    JobApplicationState.valueOf("PENDING").colorResourceId
                )
            )

            val stateIndicatorBg = stateIndicator.background as GradientDrawable
            stateIndicatorBg.setColor(
                ContextCompat.getColor(
                    itemView.context,
                    JobApplicationState.valueOf("PENDING").colorResourceId
                )
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val adapterLayout = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.row_offer_list_item, parent, false)
            return UserItemViewHolder(adapterLayout)
        }
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.loading_item_row, parent, false)

        return JobOfferItemAdapter.LoadingViewHolder(view)
    }

    override fun getItemCount(): Int = dataset?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        return if (dataset?.get(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is UserItemViewHolder) {
            holder.bind(dataset?.get(position)!!)
            bindListeners(holder.itemView, dataset?.get(position)!!)
        } else if (holder is JobOfferItemAdapter.LoadingViewHolder) {
            (holder as JobOfferItemAdapter.LoadingViewHolder?)?.let { showLoadingView(it, position) }
        }
    }

    private fun bindListeners(view: View, user: ApplicationUser) {

    }

    private fun showLoadingView(viewHolder: JobOfferItemAdapter.LoadingViewHolder, position: Int) {
        //ProgressBar would be displayed
    }

}