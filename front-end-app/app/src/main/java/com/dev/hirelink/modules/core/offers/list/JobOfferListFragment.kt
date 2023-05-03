package com.dev.hirelink.modules.core.offers.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.hirelink.R
import com.dev.hirelink.components.HttpExceptionParser
import com.dev.hirelink.data.MockDataSource
import com.dev.hirelink.databinding.FragmentJobOfferListBinding
import com.dev.hirelink.dto.BasicErrorResponse
import com.dev.hirelink.models.JobOffer
import com.dev.hirelink.models.PaginatedResourceWrapper
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.dev.hirelink.modules.core.BaseActivity
import com.dev.hirelink.modules.core.offers.list.JobOfferItemAdapter
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class JobOfferListFragment : Fragment() {
    private lateinit var binding: FragmentJobOfferListBinding
    private lateinit var jobOfferViewModel: JobOfferViewModel
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var jobOffers: List<JobOffer>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_job_offer_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        jobOfferViewModel = (requireActivity() as BaseActivity).jobOfferViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customLoadingOverlay = CustomLoadingOverlay(
            requireContext(),
            R.id.root_constraint_layout_job_offer_list,
            R.layout.loading_overlay_centered
        )

        fetchJobOffers()
    }

    private fun initRecyclerView() {
        val recyclerView = binding.recyclerViewOfferList
        recyclerView.adapter = JobOfferItemAdapter(requireContext(), jobOffers)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
    }

    private fun fetchJobOffers() {
        customLoadingOverlay.showLoading()
        val disposable = jobOfferViewModel
            .jobOfferRepository
            .findAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { customLoadingOverlay.hideLoading() }
            .subscribe(
                { data: PaginatedResourceWrapper<JobOffer> ->
                    jobOffers = data.items ?: listOf()
                    initRecyclerView()
                },
                { error: Throwable -> handleError(error) }
            )
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun handleError(error: Throwable) {
        when (error) {
            is HttpException -> {
                try {
                    val errorResponse =
                        HttpExceptionParser.parse(error, BasicErrorResponse::class.java)
                    if (errorResponse.violations?.isEmpty() == false)
                        for (violation in errorResponse.violations)
                            Snackbar.make(
                                binding.recyclerViewOfferList,
                                violation.message ?: "no message",
                                Snackbar.LENGTH_SHORT
                            ).show()

                    Log.d(javaClass.simpleName, error.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    Snackbar.make(
                        binding.recyclerViewOfferList,
                        getString(R.string.error_msg),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            }
            else -> {
                error.printStackTrace()
                Snackbar.make(
                    binding.recyclerViewOfferList,
                    getString(R.string.error_msg),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }


}