package com.dev.hirelink.modules.core.offers.list

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.hirelink.R
import com.dev.hirelink.components.HttpExceptionParser
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.databinding.FragmentJobOfferListBinding
import com.dev.hirelink.dto.BasicErrorResponse
import com.dev.hirelink.enums.RoleType
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.models.JobOffer
import com.dev.hirelink.models.WrappedPaginatedResource
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.dev.hirelink.modules.core.BaseActivity
import com.dev.hirelink.modules.core.jobapplication.JobApplicationViewModel
import com.dev.hirelink.modules.core.offers.list.filter.JobOfferFilterViewModel
import com.dev.hirelink.network.auth.AuthRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class JobOfferListFragment : Fragment() {
    private lateinit var paginatedResource: WrappedPaginatedResource<JobOffer>
    private lateinit var jobOfferItemAdapter: JobOfferItemAdapter
    private lateinit var binding: FragmentJobOfferListBinding
    private lateinit var jobOfferViewModel: JobOfferViewModel
    private lateinit var jobOfferFilterViewModel: JobOfferFilterViewModel
    private lateinit var jobApplicationViewModel: JobApplicationViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private lateinit var currentUser: ApplicationUser
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var recyclerView: RecyclerView
    private lateinit var jobOffers: MutableList<JobOffer?>
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationPermissionGranted: Boolean = false;
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    private lateinit var filterCriteria: JobOfferFilterViewModel.JobOfferFilterCriteria
    private var lastKnownLocation: Location? = null
    private lateinit var sharedPrefs: SharedPreferenceManager
    private var loaded = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                locationPermissionGranted = true
                fetchDeviceLocation()
            } else {
                Toast.makeText(requireContext(), "Permission refusée", Toast.LENGTH_SHORT).show()
            }
        }


    private var isLoading = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_job_offer_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        jobOfferViewModel = (requireActivity() as BaseActivity).jobOfferViewModel
        sharedPrefs = SharedPreferenceManager(requireContext())
        jobOfferFilterViewModel = (requireActivity() as BaseActivity).jobOfferListfilterViewModel
        jobApplicationViewModel = (requireActivity() as BaseActivity).jobApplicationViewModel
        authRepository = (requireActivity() as BaseActivity).authRepository

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customLoadingOverlay = CustomLoadingOverlay(
            requireContext(),
            R.id.root_constraint_layout_job_offer_list,
            R.layout.loading_overlay_centered
        )

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        fetchCurrentUser {
            requestLocationPermission()

            attachObservers()
        }
    }

    private fun attachObservers() {
        jobOfferFilterViewModel.criteria.observe(viewLifecycleOwner) {
            filterCriteria = it

            onCriteriaChange()
        }

        jobApplicationViewModel.jobApplicationDone.observe(viewLifecycleOwner) {
            if (it) {
                onCriteriaChange()
            }
        }
    }

    private fun onCriteriaChange() {
        fetchJobOffers { data: WrappedPaginatedResource<JobOffer> ->
            jobOffers = data.items ?: mutableListOf()
            paginatedResource = data

            if (!loaded) {
                initRecyclerView()
                loaded = true
            } else {
                isLoading = false
                jobOfferItemAdapter.dataset = jobOffers
                jobOfferItemAdapter.notifyDataSetChanged()
            }

        }
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerViewOfferList
        jobOfferItemAdapter = JobOfferItemAdapter(requireContext(), jobOffers)
        recyclerView.adapter = jobOfferItemAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        initScrollListener()
    }

    private fun fetchJobOffers(
        pageNumber: Int = 1,
        showLoader: Boolean = true,
        onDataReceived: (data: WrappedPaginatedResource<JobOffer>) -> Unit
    ) {
        if (showLoader)
            customLoadingOverlay.showLoading()

        jobOfferViewModel
            .jobOfferRepository
            .apply {
                val jobOfferObservable: Single<WrappedPaginatedResource<JobOffer>> =
                    if (currentUser.role?.code == RoleType.EMPLOYER.code || currentUser.role?.code == RoleType.INTERIM_AGENCY.code)
                        findByOwnerId(currentUser.id!!, pageNumber = pageNumber)
                    else this.findAll(
                        pageNumber = pageNumber,
                        lat = filterCriteria.latitude,
                        lng = filterCriteria.longitude,
                        maxDistance = filterCriteria.maxDistance,
                        jobTitle = filterCriteria.jobTitle,
                        minSalary = filterCriteria.minSalary,
                        maxSalary = filterCriteria.maxSalary,
                        fromDate = filterCriteria.fromDate,
                        toDate = filterCriteria.toDate,
                        companyIDs = filterCriteria.chosenCompanyIds,
                        professionIDs = filterCriteria.chosenProfessionIds
                    )


                val disposable = jobOfferObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally { customLoadingOverlay.hideLoading() }
                    .subscribe(onDataReceived) { error: Throwable -> handleError(error) }

                compositeDisposable.add(disposable)
            }

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
                if (!isLoading && layoutManager.findLastCompletelyVisibleItemPosition() == jobOffers.size - 1) {
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

        jobOffers.add(null)
        jobOfferItemAdapter.notifyItemInserted(jobOffers.size - 1)

        val nextPageUri = Uri.parse(paginatedResource.paginationView?.nextItemLink)
        val nextPageNumber = nextPageUri.getQueryParameter("page")?.toIntOrNull()

        if (nextPageNumber != null) {
            fetchJobOffers(pageNumber = nextPageNumber, showLoader = false) {
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


    private fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                locationPermissionGranted = true
                fetchDeviceLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // Afficher une boîte de dialogue pour expliquer à l'utilisateur pourquoi vous avez besoin de la permission
                AlertDialog.Builder(requireContext())
                    .setTitle("Location permission")
                    .setMessage("We need your location to show you nearby offers")
                    .setPositiveButton("Ok") { _, _ ->
                        requestPermissionLauncher.launch(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchDeviceLocation() {
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                lastKnownLocation = location
                sharedPrefs.storeCoordinates(location.latitude, location.longitude)
            }
        }
    }

    private fun fetchCurrentUser(onUserFetched: () -> Unit) {
        val disposable = authRepository
            .currentUser
            .subscribe { applicationUser ->
                currentUser = applicationUser!!
                onUserFetched()
            }

        compositeDisposable.add(disposable)
    }


}