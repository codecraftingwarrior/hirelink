package com.dev.hirelink.modules.core.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentGestionnaireDashboardBinding
import com.dev.hirelink.dto.Dashboard
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.dev.hirelink.modules.core.ManagerBaseActivity
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp.setup
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentGestionnaireDashboardBinding
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private val compositeDisposable = CompositeDisposable()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_gestionnaire_dashboard,
            container,
            false
        )
        dashboardViewModel = (requireActivity() as ManagerBaseActivity).dashboardViewModel
        customLoadingOverlay = CustomLoadingOverlay(
            requireContext(),
            R.id.gestionnaire_dashboard_constraint_layout_parent,
            R.layout.loading_overlay_centered
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
    }

    private fun setup() {
        fetchData()
    }


    private fun fetchData() {
        customLoadingOverlay.showLoading()
        dashboardViewModel
            .apply {
                val observable: Single<Dashboard> =
                    findStats()

                val disposable = observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally { customLoadingOverlay.hideLoading() }
                    .subscribe { data: Dashboard -> Log.d(javaClass.simpleName, data.toString()) }

                compositeDisposable.add(disposable)
            }
    }
}