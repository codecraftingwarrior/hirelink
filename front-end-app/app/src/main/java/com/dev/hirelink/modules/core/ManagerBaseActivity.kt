package com.dev.hirelink.modules.core

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.dev.hirelink.HirelinkApplication
import com.dev.hirelink.R
import com.dev.hirelink.databinding.ActivityManagerBaseBinding
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.modules.core.dashboard.DashboardFragment
import com.dev.hirelink.modules.core.dashboard.DashboardViewModel
import com.dev.hirelink.modules.core.offers.list.filter.JobOfferFilterViewModel
import com.dev.hirelink.network.auth.AuthRepository
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp.setup
import io.reactivex.disposables.CompositeDisposable

class ManagerBaseActivity : AppCompatActivity() {
    private val compositeDisposable = CompositeDisposable()
    val authRepository: AuthRepository by lazy { (application as HirelinkApplication).authRepository }
    private lateinit var binding: ActivityManagerBaseBinding
    private lateinit var currentUser: ApplicationUser
    val dashboardViewModel: DashboardViewModel by viewModels {
        DashboardViewModel.DashboardViewModelFactory(
            applicationContext,
            (application as HirelinkApplication).dashboardRepository,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setup()
    }

    private fun setup() {
        setupNavigationBar()

    }

    private fun setupNavigationBar() {
        binding.bottomNavigationManager.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.gestionnaire_menu_item_dashboard -> {
                    binding.textFieldSearch.visibility = View.GONE
                    replaceFragment(DashboardFragment())
                    true
                }
                R.id.gestionnaire_menu_item_users -> {
                    binding.textFieldSearch.visibility = View.VISIBLE
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigationManager.menu.getItem(0).isChecked = true
        replaceFragment(DashboardFragment())
    }

    private fun fetchCurrentUser() {
        val disposable = authRepository
            .currentUser
            .subscribe { applicationUser ->
                currentUser = applicationUser ?: ApplicationUser()
            }

        compositeDisposable.add(disposable)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.base_activity_fragment_container_manager, fragment)
        }
    }
}