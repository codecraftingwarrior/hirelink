package com.dev.hirelink.modules.core.notification

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
import com.dev.hirelink.databinding.FragmentCandidateNotificationListBinding
import com.dev.hirelink.models.JobApplication
import com.dev.hirelink.models.Notification
import com.dev.hirelink.models.WrappedPaginatedResource
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.dev.hirelink.modules.common.NoDataView
import com.dev.hirelink.modules.core.BaseActivity
import com.dev.hirelink.modules.core.schedule.ScheduleItemAdapter
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp.setup
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NotificationListFragment : Fragment() {
    private lateinit var binding: FragmentCandidateNotificationListBinding
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var notifications: MutableList<Notification?>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationItemAdapter
    private val compositeDisposable = CompositeDisposable()
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private lateinit var noDataView: NoDataView
    private lateinit var paginatedResource: WrappedPaginatedResource<Notification>
    private var isLoading = false
    private var loaded = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_candidate_notification_list,
            container,
            false
        )
        notificationViewModel = (requireActivity() as BaseActivity).notificationViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customLoadingOverlay = CustomLoadingOverlay(
            requireContext(),
            R.id.root_constraint_layout_notification_list,
            R.layout.loading_overlay_centered
        )

        noDataView = NoDataView(
            requireContext(),
            R.id.root_coordinator_layout_ja_list
        )

        setup()
    }

    private fun setup() {
        fullInitialization()
    }

    private fun fetchNotifications(
        pageNumber: Int = 1,
        showLoader: Boolean = true,
        onDataReceived: (data: WrappedPaginatedResource<Notification>) -> Unit
    ) {

        if (showLoader)
            customLoadingOverlay.showLoading()

        notificationViewModel
            .apply {
                val observable: Single<WrappedPaginatedResource<Notification>> =
                    fetchNotifications(page = pageNumber)

                val disposable = observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally { customLoadingOverlay.hideLoading() }
                    .subscribe(onDataReceived) { error: Throwable -> error.printStackTrace() }

                compositeDisposable.add(disposable)
            }
    }

    private fun fullInitialization() {
        fetchNotifications {
            notifications = it.items ?: mutableListOf()
            paginatedResource = it

            if (!loaded) {
                initRecyclerView()
                loaded = true
            } else {
                isLoading = false
                adapter.dataset = notifications
                adapter.notifyDataSetChanged()
            }

            if (notifications.isEmpty()) {
                noDataView.show()
            } else {
                noDataView.hide()
            }
        }
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerViewNotificationList
        adapter = NotificationItemAdapter(requireContext(), notifications.toMutableList())
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
                if (!isLoading && layoutManager.findLastCompletelyVisibleItemPosition() == notifications.size - 1) {
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

        notifications.add(null)
        adapter.notifyItemInserted(notifications.size - 1)

        val nextPageUri = Uri.parse(paginatedResource.paginationView?.nextItemLink)
        val nextPageNumber = nextPageUri.getQueryParameter("page")?.toIntOrNull()
        if (nextPageNumber != null) {
            fetchNotifications(pageNumber = nextPageNumber, showLoader = false) {
                notifications.removeAt(notifications.size - 1)
                val scrollPosition: Int = notifications.size
                adapter.notifyItemRemoved(scrollPosition)

                it.items?.let { items -> notifications.addAll(items) }

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