package com.dev.hirelink.modules.core.jobapplication.list

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentJobApplicationListBinding
import com.dev.hirelink.enums.JobApplicationState
import com.dev.hirelink.models.JobApplication
import com.dev.hirelink.models.WrappedPaginatedResource
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.dev.hirelink.modules.common.NoDataView
import com.dev.hirelink.modules.core.BaseActivity
import com.dev.hirelink.modules.core.jobapplication.JobApplicationViewModel
import com.google.android.material.tabs.TabLayout
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class JobApplicationListFragment : Fragment() {
    private val stateMap = mapOf(
        JobApplicationState.REFUSED.name to "Refused",
        JobApplicationState.ACCEPTED.name to "Accepted",
        JobApplicationState.PENDING.name to "Pending",
    )
    private var jobApplicationFilterCriteria =
        JobApplicationViewModel.JobApplicationFilterCriteria()
    private lateinit var binding: FragmentJobApplicationListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JobApplicationItemAdapter
    private val compositeDisposable = CompositeDisposable()
    private var chosenState: String? = JobApplicationState.PENDING.name
    private var isLoading = false
    private var loaded = false
    private lateinit var jobApplicationViewModel: JobApplicationViewModel
    private lateinit var jobApplications: MutableList<JobApplication?>
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private lateinit var noDataView: NoDataView
    private lateinit var paginatedResource: WrappedPaginatedResource<JobApplication>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_job_application_list,
            container,
            false
        )
        jobApplicationViewModel = (requireActivity() as BaseActivity).jobApplicationViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customLoadingOverlay = CustomLoadingOverlay(
            requireContext(),
            R.id.root_coordinator_layout_ja_list,
            R.layout.loading_overlay_centered
        )

        noDataView = NoDataView(
            requireContext(),
            R.id.root_coordinator_layout_ja_list
        )

        setup()
    }

    private fun setup() {
        bindListeners()
        fullInitialization()
        attachObservers()
    }

    private fun attachObservers() {
        jobApplicationViewModel.criteria.observe(viewLifecycleOwner) {
            jobApplicationFilterCriteria = it

            fullInitialization()
        }
    }

    private fun fullInitialization() {
        fetchJobApplications {
            jobApplications = it.items ?: mutableListOf()
            paginatedResource = it

            if (!loaded) {
                initRecyclerView()
                loaded = true
            } else {
                isLoading = false
                adapter.dataset = jobApplications
            }
            adapter.notifyDataSetChanged()

            if (jobApplications.isEmpty()) {
                noDataView.show()
            } else {
                noDataView.hide()
            }
        }
    }

    private fun bindListeners() {
        binding.tabLayoutMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text.toString()) {
                    stateMap[JobApplicationState.ACCEPTED.name] -> chosenState =
                        JobApplicationState.ACCEPTED.name
                    stateMap[JobApplicationState.REFUSED.name] -> chosenState =
                        JobApplicationState.REFUSED.name
                    stateMap[JobApplicationState.PENDING.name] -> chosenState =
                        JobApplicationState.PENDING.name
                }
                if (chosenState != null)
                    fullInitialization()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }

    private fun fetchJobApplications(
        pageNumber: Int = 1,
        showLoader: Boolean = true,
        onDataReceived: (data: WrappedPaginatedResource<JobApplication>) -> Unit
    ) {

        if (showLoader)
            customLoadingOverlay.showLoading()

        jobApplicationViewModel
            .apply {
                val jobOfferObservable: Single<WrappedPaginatedResource<JobApplication>> =
                    findJobApplications(
                        page = pageNumber,
                        state = chosenState,
                        searchQuery = jobApplicationFilterCriteria.searchQuery
                    )

                val disposable = jobOfferObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally { customLoadingOverlay.hideLoading() }
                    .subscribe(onDataReceived) { error: Throwable -> error.printStackTrace() }

                compositeDisposable.add(disposable)
            }
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerViewJobApplicationList
        adapter = JobApplicationItemAdapter(requireContext(), jobApplications.toMutableList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        initScrollListener()
    }

    private fun initScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (!isLoading && layoutManager.findLastCompletelyVisibleItemPosition() == jobApplications.size - 1) {
                    loadMore()
                    isLoading = true
                }
            }
        })
    }

    private fun loadMore() {
        if (paginatedResource.paginationView?.nextItemLink == null) {
            isLoading = false
            return
        }

        jobApplications.add(null)
        adapter.notifyItemInserted(jobApplications.size - 1)

        val nextPageUri = Uri.parse(paginatedResource.paginationView?.nextItemLink)
        val nextPageNumber = nextPageUri.getQueryParameter("page")?.toIntOrNull()
        if (nextPageNumber != null) {
            fetchJobApplications(pageNumber = nextPageNumber, showLoader = false) {
                jobApplications.removeAt(jobApplications.size - 1)
                val scrollPosition: Int = jobApplications.size
                adapter.notifyItemRemoved(scrollPosition)

                it.items?.let { items -> jobApplications.addAll(items) }

                paginatedResource = it
                adapter.notifyItemRangeChanged(scrollPosition, it.items?.size ?: 0)
                isLoading = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}