package com.dev.hirelink.modules.core

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.dev.hirelink.HirelinkApplication
import com.dev.hirelink.R
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.databinding.ActivityManagerBaseBinding
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.modules.core.dashboard.DashboardFragment
import com.dev.hirelink.modules.core.dashboard.DashboardViewModel
import com.dev.hirelink.modules.core.offers.list.filter.JobOfferFilterViewModel
import com.dev.hirelink.modules.core.user.UserListFragment
import com.dev.hirelink.network.auth.AuthRepository
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp.setup
import io.reactivex.disposables.CompositeDisposable

class ManagerBaseActivity : AppCompatActivity() {
    private val compositeDisposable = CompositeDisposable()
    val authRepository: AuthRepository by lazy { (application as HirelinkApplication).authRepository }
    private lateinit var binding: ActivityManagerBaseBinding
    private lateinit var sharedPrefs: SharedPreferenceManager
    private lateinit var currentUser: ApplicationUser
    val dashboardViewModel: DashboardViewModel by viewModels {
        DashboardViewModel.DashboardViewModelFactory(
            applicationContext,
            (application as HirelinkApplication).dashboardRepository,
            (application as HirelinkApplication).userRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPrefs = SharedPreferenceManager(applicationContext)

        supportActionBar?.hide()

        setup()
    }

    private fun setup() {
        setupNavigationBar()

        binding.imgBtnLogout.setOnClickListener {
            sharedPrefs.removeJwtToken()
            val authRepository = (application as HirelinkApplication).authRepository
            authRepository.emitUser(ApplicationUser())
            startActivity(Intent(this, BaseActivity::class.java))
        }

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
                    binding.textFieldSearch.visibility = View.GONE
                    binding.searchHeader.background =
                        ContextCompat.getDrawable(this, R.drawable.rectangle_bg_gray_reg)
                    replaceFragment(UserListFragment())
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigationManager.menu.getItem(0).isChecked = true
        binding.textFieldSearch.visibility = View.GONE
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