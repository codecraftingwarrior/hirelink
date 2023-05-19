package com.dev.hirelink.modules.core.offers.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.components.dateAsString
import com.dev.hirelink.components.toTimeAgo
import com.dev.hirelink.databinding.FragmentBottomSheetJobOfferDetailBinding
import com.dev.hirelink.models.JobOffer
import com.dev.hirelink.modules.core.BaseActivity
import com.dev.hirelink.modules.core.offers.list.JobOfferViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class JobOfferBottomSheetDetailFragment(
    private val jobOffer: JobOffer
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetJobOfferDetailBinding
    private lateinit var jobOfferViewModel: JobOfferViewModel
    private lateinit var fetchedJobOffer: JobOffer
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bottom_sheet_job_offer_detail,
            container,
            false
        )
        jobOfferViewModel = (requireActivity() as BaseActivity).jobOfferViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchJobOfferById()
    }

    private fun fetchJobOfferById() {
        showLoader()
        val disposable = jobOfferViewModel
            .jobOfferRepository
            .findById(jobOffer.id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { hideLoader() }
            .subscribe(
                { fetchedJobOffer ->
                    this.fetchedJobOffer = fetchedJobOffer
                    initLayout()
                },
                { error: Throwable ->
                    error.printStackTrace()
                    Snackbar.make(
                        binding.root,
                        getString(R.string.error_msg),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            )
        compositeDisposable.add(disposable)

    }

    private fun initLayout() {
        with(binding) {
            val timeago = toTimeAgo(jobOffer.createdAt ?: "2023-05-02T13:50:37+00:00")
            textViewCompanyNameJobOfferDetail.text = fetchedJobOffer.owner?.company?.name
            textViewJobTitleJobOfferDetail.text = fetchedJobOffer.title
            textViewCompanyInitialsJobOfferDetail.text =
                fetchedJobOffer.owner?.company?.name?.get(0).toString()
            textViewCompanyLocationJobOfferDetail.text = fetchedJobOffer.address
            textViewPublishedAtJobOfferDetail.text = if (fetchedJobOffer.applicantCount!! > 0) {
                getString(R.string.published_at_applicants, timeago, fetchedJobOffer.applicantCount)
            } else {
                getString(R.string.published_at_applicants_first, timeago)
            }

            textViewContractTypeJobOfferDetail.text = getString(
                R.string.offer_category_contract_profession,
                fetchedJobOffer.contractType?.name,
                fetchedJobOffer.category?.name,
                fetchedJobOffer.profession?.name
            )
            textViewSalaryRangeJobOfferDetail.text = getString(
                R.string.salary_range_str,
                fetchedJobOffer.minSalary,
                fetchedJobOffer.maxSalary
            )

            textViewDateRangeJobOfferDetail.text = getString(
                R.string.date_range,
                dateAsString(fetchedJobOffer.fromDate ?: "2023-05-02T13:50:37+00:00"),
                dateAsString(fetchedJobOffer.toDate ?: "2023-05-02T13:50:37+00:00")
            )

            textViewOfferDetailDescription.text = fetchedJobOffer.description
        }
    }

    private fun showLoader() {
        binding.progressBarJobOfferFetching.visibility = View.VISIBLE
        val rootConstraintLayout = binding.jobOfferDetailRootConstraintLayout

        for (i in 0 until rootConstraintLayout.childCount) {
            val child: View = rootConstraintLayout.getChildAt(i)
            if (child.id != R.id.progress_bar_job_offer_fetching)
                child.visibility = View.GONE
        }
    }

    private fun hideLoader() {
        binding.progressBarJobOfferFetching.visibility = View.GONE
        val rootConstraintLayout = binding.jobOfferDetailRootConstraintLayout

        for (i in 0 until rootConstraintLayout.childCount) {
            val child: View = rootConstraintLayout.getChildAt(i)
            if (child.id != R.id.progress_bar_job_offer_fetching)
                child.visibility = View.VISIBLE
        }
    }

    companion object {
        val TAG = JobOfferBottomSheetDetailFragment::class.java.simpleName
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}