package com.dev.hirelink.modules.core.user

import androidx.fragment.app.Fragment
import com.dev.hirelink.databinding.FragmentGestionnaireDashboardBinding
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.dev.hirelink.modules.core.dashboard.DashboardViewModel
import io.reactivex.disposables.CompositeDisposable

class UserListFragment: Fragment() {
    private lateinit var binding: FragmentGestionnaireDashboardBinding
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private val compositeDisposable = CompositeDisposable()
}