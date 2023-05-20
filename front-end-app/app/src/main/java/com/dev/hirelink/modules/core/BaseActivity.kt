package com.dev.hirelink.modules.core

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.dev.hirelink.HirelinkApplication
import com.dev.hirelink.R
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.databinding.ActivityBaseBinding
import com.dev.hirelink.enums.RoleType
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.modules.auth.login.LoginActivity
import com.dev.hirelink.modules.core.employer.EmployerProfilActivity
import com.dev.hirelink.modules.core.jobapplication.JobApplicationViewModel
import com.dev.hirelink.modules.core.jobapplication.list.JobApplicationListFragment
import com.dev.hirelink.modules.core.offers.list.JobOfferListFragment
import com.dev.hirelink.modules.core.offers.list.JobOfferViewModel
import com.dev.hirelink.modules.core.profil.ProfilActivity
import com.dev.hirelink.modules.core.offers.list.filter.JobOfferFilterBottomSheetFragment
import com.dev.hirelink.modules.core.offers.list.filter.JobOfferFilterViewModel
import com.dev.hirelink.network.auth.AuthRepository
import com.dev.hirelink.network.jobapplication.JobApplicationRepository
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Job

class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding
    val authRepository: AuthRepository by lazy { (application as HirelinkApplication).authRepository }
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    lateinit var currentUser: ApplicationUser
    private lateinit var jobOfferFilterCriteria: JobOfferFilterViewModel.JobOfferFilterCriteria
    private var isLoggedIn = false
    private lateinit var sharedPrefs: SharedPreferenceManager
    val jobOfferListfilterViewModel: JobOfferFilterViewModel by viewModels {
        JobOfferFilterViewModel.JobOfferFilterViewModelFactory(
            applicationContext,
            (application as HirelinkApplication).companyRepository,
            (application as HirelinkApplication).professionRepository
        )
    }
    val jobOfferViewModel: JobOfferViewModel by viewModels {
        JobOfferViewModel.JobOfferViewModelFactory(
            applicationContext,
            (application as HirelinkApplication).jobOfferRepository
        )
    }
    val jobApplicationViewModel: JobApplicationViewModel by viewModels {
        JobApplicationViewModel.JobApplicationViewModelFactory(
            applicationContext,
            (application as HirelinkApplication).jobApplicationRepository
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

        //binding.chipAll.isChecked = true

        jobOfferFilterCriteria = jobOfferListfilterViewModel.getCriteria()

        jobOfferListfilterViewModel.updateCriteria(jobOfferFilterCriteria)

        jobApplicationViewModel.jobApplicationDone.observe(this) {
            if (it)
                Snackbar.make(
                    binding.bottomNavigation,
                    getString(R.string.job_application_success_msg),
                    Snackbar.LENGTH_LONG
                ).show()
        }
    }

    private fun fetchCurrentUser() {
        val disposable = authRepository
            .currentUser
            .subscribe { applicationUser ->
                currentUser = applicationUser ?: ApplicationUser()
                isLoggedIn = applicationUser?.id != null
                if (isLoggedIn) {
                    binding.imgBtnProfile.visibility = View.VISIBLE
                    binding.buttonLogin.visibility = View.GONE
                    binding.bottomNavigation.visibility =
                        if (currentUser.role?.code == RoleType.APPLICANT.code) View.VISIBLE else View.GONE
                    if (currentUser.role?.code != RoleType.APPLICANT.code) {
                        binding.chipGroupDistanceFilter.visibility = View.GONE
                        binding.addFloatingActionButton.visibility = View.VISIBLE
                    } else {
                        binding.chipGroupDistanceFilter.visibility = View.VISIBLE
                        binding.addFloatingActionButton.visibility = View.GONE
                    }
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
            if (currentUser.role?.code == RoleType.APPLICANT.code)
                startActivity(Intent(this, ProfilActivity::class.java))
            else
                startActivity(Intent(this, EmployerProfilActivity::class.java))
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

        setupSearchField()

    }

    private fun setupSearchField() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s == null || s.isEmpty()) {
                    jobOfferFilterCriteria.jobTitle = null
                    jobOfferListfilterViewModel.updateCriteria(jobOfferFilterCriteria)
                } else if (s.length >= 4) {
                    jobOfferFilterCriteria.jobTitle = s.toString()
                    jobOfferListfilterViewModel.updateCriteria(jobOfferFilterCriteria)
                }

            }
        }

        binding.editTextJobOfferListSearch.addTextChangedListener(watcher)
    }

    private fun fetchClosestOffers(chipGroup: ChipGroup) {
        val currentCheckedChipId = findViewById<Chip>(chipGroup.checkedChipId)
        val maxDistance = when (currentCheckedChipId?.text.toString()) {
            getString(R.string._10_km) -> 10
            getString(R.string._20_km) -> 20
            getString(R.string._30_km) -> 30
            getString(R.string._40_km) -> 40
            getString(R.string._50_km) -> 50
            else -> null
        }

        if (maxDistance != null) {
            jobOfferFilterCriteria.latitude = sharedPrefs.deviceLatitude()
            jobOfferFilterCriteria.longitude = sharedPrefs.deviceLongitude()
            jobOfferFilterCriteria.maxDistance = maxDistance.toDouble()
        } else {
            jobOfferFilterCriteria.latitude = null
            jobOfferFilterCriteria.longitude = null
            jobOfferFilterCriteria.maxDistance = null
        }

        jobOfferListfilterViewModel.updateCriteria(jobOfferFilterCriteria)
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