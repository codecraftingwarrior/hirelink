package com.dev.hirelink.modules.core.offers.list

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.hirelink.R
import com.dev.hirelink.components.HttpExceptionParser
import com.dev.hirelink.databinding.FragmentJobOfferListBinding
import com.dev.hirelink.dto.BasicErrorResponse
import com.dev.hirelink.models.JobOffer
import com.dev.hirelink.models.PaginatedResourceWrapper
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.dev.hirelink.modules.core.BaseActivity
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class JobOfferListFragment : Fragment() {
    private lateinit var paginatedResource: PaginatedResourceWrapper<JobOffer>
    private lateinit var jobOfferItemAdapter: JobOfferItemAdapter
    private lateinit var binding: FragmentJobOfferListBinding
    private lateinit var jobOfferViewModel: JobOfferViewModel
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var recyclerView: RecyclerView
    private lateinit var jobOffers: MutableList<JobOffer?>
    private var isLoading = false
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

        fetchJobOffers { data: PaginatedResourceWrapper<JobOffer> ->
            jobOffers = data.items ?: mutableListOf()
            paginatedResource = data
            initRecyclerView()
            initScrollListener()
        }

    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerViewOfferList
        jobOfferItemAdapter = JobOfferItemAdapter(requireContext(), jobOffers)
        recyclerView.adapter = jobOfferItemAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
    }

    private fun fetchJobOffers(
        pageNumber: Int = 1,
        onDataReceived: (data: PaginatedResourceWrapper<JobOffer>) -> Unit
    ) {
        customLoadingOverlay.showLoading()
        val disposable = jobOfferViewModel
            .jobOfferRepository
            .findAll(pageNumber = pageNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { customLoadingOverlay.hideLoading() }
            .subscribe(onDataReceived) { error: Throwable -> handleError(error) }
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun initScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                if (!isLoading && layoutManager.findLastCompletelyVisibleItemPosition() + recyclerView.childCount >= layoutManager.itemCount) {
                    loadMore()
                    isLoading = true
                }
            }
        })
    }

    private fun loadMore() {
        if (paginatedResource.paginationView?.nextItemLink == null)
            return

        jobOffers.add(null)
        jobOfferItemAdapter.notifyItemInserted(jobOffers.size - 1)

        val nextPageUri = Uri.parse(paginatedResource.paginationView?.nextItemLink)
        val nextPageNumber = nextPageUri.getQueryParameter("page")?.toIntOrNull()

        if (nextPageNumber != null) {
            fetchJobOffers(pageNumber = nextPageNumber) {
                jobOffers.removeAt(jobOffers.size - 1)
                val scrollPosition: Int = jobOffers.size
                jobOfferItemAdapter.notifyItemRemoved(scrollPosition)

                it.items?.let { items -> jobOffers.addAll(items) }

                paginatedResource = it
                jobOfferItemAdapter.notifyItemRangeChanged(scrollPosition, it.items?.size ?: 0)
                isLoading = false
            }
        }
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