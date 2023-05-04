package com.dev.hirelink.modules.core

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.dev.hirelink.HirelinkApplication
import com.dev.hirelink.R
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.databinding.ActivityBaseBinding
import com.dev.hirelink.modules.auth.login.LoginActivity
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.dev.hirelink.modules.core.jobapplication.list.JobApplicationListFragment
import com.dev.hirelink.modules.core.offers.list.JobOfferListFragment
import com.dev.hirelink.modules.core.offers.list.JobOfferViewModel
import com.dev.hirelink.modules.core.profil.ProfilActivity
import com.dev.hirelink.modules.core.offers.list.filter.JobOfferFilterBottomSheetFragment
import com.dev.hirelink.modules.core.offers.list.filter.JobOfferFilterViewModel
import com.dev.hirelink.network.auth.AuthRepository
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import io.reactivex.disposables.CompositeDisposable

class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding
    private val authRepository: AuthRepository by lazy { (application as HirelinkApplication).authRepository }
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var isLoggedIn = false
    private lateinit var sharedPrefs: SharedPreferenceManager
    val jobOfferListfilterViewModel: JobOfferFilterViewModel by viewModels {
        JobOfferFilterViewModel.JobOfferFilterViewModelFactory(applicationContext)
    }
    val jobOfferViewModel: JobOfferViewModel by viewModels {
        JobOfferViewModel.JobOfferViewModelFactory(
            applicationContext,
            (application as HirelinkApplication).jobOfferRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater);
        fetchCurrentUser()
        setContentView(binding.root)

        sharedPrefs = SharedPreferenceManager(applicationContext)

        supportActionBar?.hide()

        setupNavigationBar();
        bindListeners()
        jobOfferListfilterViewModel.updateCriteria(JobOfferFilterViewModel.JobOfferFilterCriteria())
    }

    private fun fetchCurrentUser() {
        val disposable = authRepository
            .currentUser
            .subscribe { applicationUser ->
                isLoggedIn = applicationUser?.id != null
                if (isLoggedIn) {
                    binding.imgBtnProfile.visibility = View.VISIBLE
                    binding.buttonLogin.visibility = View.GONE
                    binding.bottomNavigation.visibility = View.VISIBLE
                } else {
                    binding.imgBtnProfile.visibility = View.GONE
                    binding.buttonLogin.visibility = View.VISIBLE
                    binding.bottomNavigation.visibility = View.GONE
                }
            }

        compositeDisposable.add(disposable)
    }

    private fun bindListeners() {
        binding.rootConstraintLayoutActivityBase.apply {
            setOnClickListener {
                hideKeyboard(
                    this
                )
            }
        }
        binding.imgBtnFilter.setOnClickListener {
            (JobOfferFilterBottomSheetFragment()).show(
                supportFragmentManager,
                JobOfferFilterBottomSheetFragment.TAG
            )
        }

        binding.imgBtnProfile.setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
        }

        binding.buttonLogin.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )
        }

        binding.chipGroupDistanceFilter.setOnCheckedStateChangeListener { chipGroup, _ ->
            fetchClosestOffers(chipGroup)
        }

    }

    private fun fetchClosestOffers(chipGroup: ChipGroup) {
        val maxDistance = when (findViewById<Chip>(chipGroup.checkedChipId).text.toString()) {
            getString(R.string._10_km) -> 10
            getString(R.string._20_km) -> 20
            getString(R.string._30_km) -> 30
            getString(R.string._40_km) -> 40
            getString(R.string._50_km) -> 50
            else -> null
        }
        val criteria = JobOfferFilterViewModel.JobOfferFilterCriteria()

        if (maxDistance != null) {
            criteria.latitude = sharedPrefs.deviceLatitude()
            criteria.longitude = sharedPrefs.deviceLongitude()
            criteria.maxDistance = maxDistance.toDouble()
        }

        jobOfferListfilterViewModel.updateCriteria(criteria)
    }

    private fun setupNavigationBar() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            binding.searchHeader.background =
                ContextCompat.getDrawable(this, R.drawable.rectangle_bg_gray)
            when (item.itemId) {
                R.id.menu_item_schedule -> {
                    binding.horizontalScrollViewChipDistance.visibility = View.GONE

                    true
                }
                R.id.menu_item_candidacy -> {
                    // Respond to navigation item 2 click
                    binding.searchHeader.background =
                        ContextCompat.getDrawable(this, R.drawable.rectangle_bg_gray_reg)
                    binding.horizontalScrollViewChipDistance.visibility = View.GONE

                    replaceFragment(JobApplicationListFragment())
                    true
                }
                R.id.menu_item_offers -> {
                    binding.horizontalScrollViewChipDistance.visibility = View.VISIBLE
                    replaceFragment(JobOfferListFragment())
                    true
                }
                R.id.menu_item_notifications -> {
                    binding.horizontalScrollViewChipDistance.visibility = View.VISIBLE
                    Log.d(javaClass.simpleName, "Notifications is clicked")
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.menu.getItem(2).isChecked = true
        replaceFragment(JobOfferListFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.base_activity_fragment_container, fragment)
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}