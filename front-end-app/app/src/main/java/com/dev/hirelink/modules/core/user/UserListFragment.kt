package com.dev.hirelink.modules.core.user

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentGestionnaireDashboardBinding
import com.dev.hirelink.databinding.FragmentManagerUserListBinding
import com.dev.hirelink.enums.JobApplicationState
import com.dev.hirelink.enums.RoleType
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.models.JobApplication
import com.dev.hirelink.models.WrappedPaginatedResource
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.dev.hirelink.modules.common.NoDataView
import com.dev.hirelink.modules.core.ManagerBaseActivity
import com.dev.hirelink.modules.core.dashboard.DashboardViewModel
import com.dev.hirelink.modules.core.jobapplication.list.JobApplicationItemAdapter
import com.google.android.material.tabs.TabLayout
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp.setup
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class UserListFragment: Fragment() {
    private val roleMap = mapOf(
        RoleType.APPLICANT.code to "Applicant",
        RoleType.INTERIM_AGENCY.code to "Agency",
        RoleType.EMPLOYER.code to "Employer",
    )
    private lateinit var binding: FragmentManagerUserListBinding
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private val compositeDisposable = CompositeDisposable()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserListItemAdapter
    private var chosenRole: String? = RoleType.INTERIM_AGENCY.code
    private var isLoading = false
    private var loaded = false
    private lateinit var noDataView: NoDataView
    private lateinit var paginatedResource: WrappedPaginatedResource<ApplicationUser>
    private lateinit var users: MutableList<ApplicationUser?>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_manager_user_list,
            container,
            false
        )
        dashboardViewModel = (requireActivity() as ManagerBaseActivity).dashboardViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customLoadingOverlay = CustomLoadingOverlay(
            requireContext(),
            R.id.root_coordinator_layout_user_list,
            R.layout.loading_overlay_centered
        )

        noDataView = NoDataView(
            requireContext(),
            R.id.root_coordinator_layout_user_list
        )

        setup()
    }

    private fun setup() {
        bindListeners()
        fullInitialization()
    }

    private fun fullInitialization() {
        fetchUsers {
            users = it.items ?: mutableListOf()
            paginatedResource = it

            if (!loaded) {
                initRecyclerView()
                loaded = true
            } else {
                isLoading = false
                adapter.dataset = users
            }
            adapter.notifyDataSetChanged()

            if (users.isEmpty()) {
                noDataView.show()
            } else {
                noDataView.hide()
            }
        }
    }

    private fun bindListeners() {
        binding.tabLayoutMainUser.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text.toString()) {
                    roleMap[RoleType.APPLICANT.code] -> chosenRole = RoleType.APPLICANT.code
                    roleMap[RoleType.INTERIM_AGENCY.code] -> chosenRole = RoleType.INTERIM_AGENCY.code
                    roleMap[RoleType.EMPLOYER.code] -> chosenRole = RoleType.EMPLOYER.code
                }
                if (chosenRole != null)
                    fullInitialization()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }

    private fun fetchUsers(
        pageNumber: Int = 1,
        showLoader: Boolean = true,
        onDataReceived: (data: WrappedPaginatedResource<ApplicationUser>) -> Unit
    ) {

        if (showLoader)
            customLoadingOverlay.showLoading()

        dashboardViewModel
            .apply {
                val observable: Single<WrappedPaginatedResource<ApplicationUser>> =
                    findAllUsers(
                        page = pageNumber,
                        role = chosenRole,
                    )

                val disposable = observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally { customLoadingOverlay.hideLoading() }
                    .subscribe(onDataReceived) { error: Throwable -> error.printStackTrace() }

                compositeDisposable.add(disposable)
            }
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerViewUserList
        adapter = UserListItemAdapter(requireContext(), users.toMutableList())
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
                if (!isLoading && layoutManager.findLastCompletelyVisibleItemPosition() == users.size - 1) {
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

        users.add(null)
        adapter.notifyItemInserted(users.size - 1)

        val nextPageUri = Uri.parse(paginatedResource.paginationView?.nextItemLink)
        val nextPageNumber = nextPageUri.getQueryParameter("page")?.toIntOrNull()
        if (nextPageNumber != null) {
            fetchUsers(pageNumber = nextPageNumber, showLoader = false) {
                users.removeAt(users.size - 1)
                val scrollPosition: Int = users.size
                adapter.notifyItemRemoved(scrollPosition)

                it.items?.let { items -> users.addAll(items) }

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