package com.dev.hirelink.modules.core.offers.create

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.dev.hirelink.HirelinkApplication
import com.dev.hirelink.R
import com.dev.hirelink.databinding.ActivityCreateJobOfferBinding
import com.dev.hirelink.models.*
import com.dev.hirelink.modules.common.CustomLoadingOverlay
import com.dev.hirelink.modules.core.offers.JobOfferViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.mapbox.search.autofill.AddressAutofill
import com.mapbox.search.autofill.AddressAutofillSuggestion
import com.mapbox.search.autofill.Query
import com.mapbox.search.ui.adapter.autofill.AddressAutofillUiAdapter
import com.mapbox.search.ui.view.CommonSearchViewConfiguration
import com.mapbox.search.ui.view.DistanceUnitType
import com.mapbox.search.ui.view.SearchResultsView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.properties.Delegates
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

class CreateJobOfferActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateJobOfferBinding
    private lateinit var addressAutofill: AddressAutofill
    private lateinit var searchResultsView: SearchResultsView
    private lateinit var addressAutofillUiAdapter: AddressAutofillUiAdapter
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private var contractTypes = listOf<ContractType>()
    private var categories = listOf<JobOfferCategory>()
    private var professions = listOf<Profession>()
    private val jobOfferViewModel: JobOfferViewModel by viewModels {
        JobOfferViewModel.JobOfferViewModelFactory(
            applicationContext,
            (application as HirelinkApplication).jobOfferRepository,
            (application as HirelinkApplication).contractTypeRepository,
            (application as HirelinkApplication).jobCategoryRepository,
            (application as HirelinkApplication).professionRepository,
            (application as HirelinkApplication).authRepository,
        )
    }
    private var selectedStartDate by Delegates.notNull<Long>()
    private val jobOffer: JobOffer by lazy {
        JobOffer()
    }
    private lateinit var currentUser: ApplicationUser
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateJobOfferBinding.inflate(layoutInflater)
        fetchCurrentUser()
        customLoadingOverlay = CustomLoadingOverlay(
            applicationContext,
            R.id.constraint_layout_root_job_offer_new,
            R.layout.loading_overlay_centered
        )

        supportActionBar?.hide()

        setContentView(binding.root)

        fetchData()

        bindListeners()

        setupAddressAutofillField()

    }

    private fun fetchData() {
        customLoadingOverlay.showLoading()
        val contractDisposable = fetchContractTypes()
        val categoryDisposable = fetchJobCategories()
        val professionDisposable = fetchProfessions()

        val disposable = io.reactivex.Observable.zip(
            contractDisposable,
            categoryDisposable,
            professionDisposable
        ) { contracts, categories, professions ->
            if (this.contractTypes.isEmpty() && contracts.isNotEmpty()) {
                contractTypes = contracts
                val items = contractTypes.map { c -> c.name }
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
                (binding.editTextNewOfferContractType as? AutoCompleteTextView)?.setAdapter(adapter)
            }

            if (this.categories.isEmpty() && categories.isNotEmpty()) {
                this.categories = categories
                val items = categories.map { c -> c.name }
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
                (binding.editTextNewOfferCategory as? AutoCompleteTextView)?.setAdapter(adapter)
            }

            if (this.professions.isEmpty() && professions.isNotEmpty()) {
                this.professions = professions
                val items = professions.map { c -> c.name }
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
                (binding.editTextNewOfferProfession as? AutoCompleteTextView)?.setAdapter(adapter)
            }

            true
            // ...
        }.doFinally { customLoadingOverlay.hideLoading() }
            .subscribe(
                { /* Succès */ },
                { error: Throwable -> error.printStackTrace() }
            )

        compositeDisposable.add(disposable)
    }

    private fun bindListeners() {
        binding.imgBtnArrowLeft.setOnClickListener { finish() }

        binding.editTextNewOfferStartDate.setOnClickListener {

            val dateValidatorMin: CalendarConstraints.DateValidator =
                DateValidatorPointForward.from(Calendar.getInstance().timeInMillis)

            val constraints: CalendarConstraints =
                CalendarConstraints.Builder()
                    .setValidator(dateValidatorMin)
                    .build()

            showDatePicker(constraints) { selectedDate ->
                val startDate =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(selectedDate))

                val startDateFormatted =
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDate))

                jobOffer.fromDate = startDate

                selectedStartDate = selectedDate

                binding.textFieldNewOfferEndDate.visibility = View.VISIBLE

                binding.editTextNewOfferStartDate.setText(startDateFormatted)
            }
        }

        binding.editTextNewOfferEndDate.setOnClickListener {
            val dateValidatorMin: CalendarConstraints.DateValidator =
                DateValidatorPointForward.from(selectedStartDate + 1.days.toLong(DurationUnit.MILLISECONDS))

            val constraints: CalendarConstraints =
                CalendarConstraints.Builder()
                    .setValidator(dateValidatorMin)
                    .build()

            showDatePicker(constraints) { selectedDate ->
                val endDate = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(Date(selectedDate))

                val endDateFormatted = SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                ).format(Date(selectedDate))

                binding.editTextNewOfferEndDate.setText(endDateFormatted)

                jobOffer.toDate = endDate
            }
        }

        binding.editTextNewOfferContractType.setOnItemClickListener { parent, _, position, _ ->
            jobOffer.contractType = contractTypes[position]
        }

        binding.editTextNewOfferCategory.setOnItemClickListener { parent, _, position, _ ->
            jobOffer.category = categories[position]
        }

        binding.editTextNewOfferProfession.setOnItemClickListener { parent, _, position, _ ->
            jobOffer.profession = professions[position]
        }

        binding.buttonJobOfferNew.setOnClickListener { createJobOffer() }
    }

    private fun createJobOffer() {
        customLoadingOverlay.showLoading()
        jobOffer.apply {
            title = binding.editTextNewOfferTitle.text.toString()
            description = binding.editTextNewOfferDescription.text.toString()
            minSalary = binding.editTextNewOfferMinSalary.text.toString().toFloat()
            maxSalary = binding.editTextNewOfferMaxSalary.text.toString().toFloat()
            owner = currentUser
        }

        Log.d(javaClass.simpleName, jobOffer.toString())

        val disposable = jobOfferViewModel
            .createJobOffer(jobOffer)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { customLoadingOverlay.hideLoading() }
            .subscribe(
                { newJobOffer ->
                    Log.d(javaClass.simpleName, newJobOffer.toString())
                    jobOfferViewModel.onAddedJobOffer(newJobOffer)
                    finish()
                },
                { error: Throwable -> error.printStackTrace() }
            )

        compositeDisposable.add(disposable)
    }

    private fun showDatePicker(
        constraints: CalendarConstraints,
        onDateChoosen: (date: Long) -> Unit
    ) {


        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(resources.getString(R.string.date_select_prompt))
            .setCalendarConstraints(constraints)
            .build()




        datePicker.addOnPositiveButtonClickListener(onDateChoosen)

        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun fetchCurrentUser() {
        val disposable = jobOfferViewModel
            .authRepository
            .currentUser
            .subscribe { applicationUser -> currentUser = applicationUser!! }

        compositeDisposable.add(disposable)
    }

    private fun setupAddressAutofillField() {
        addressAutofill = AddressAutofill.create(getString(R.string.mapbox_access_token))

        searchResultsView = binding.searchNewOfferResultsView

        searchResultsView.initialize(
            SearchResultsView.Configuration(
                commonConfiguration = CommonSearchViewConfiguration(DistanceUnitType.IMPERIAL)
            )
        )

        addressAutofillUiAdapter = AddressAutofillUiAdapter(
            view = searchResultsView,
            addressAutofill = addressAutofill
        )

        addressAutofillUiAdapter.addSearchListener(object :
            AddressAutofillUiAdapter.SearchListener {

            override fun onSuggestionSelected(suggestion: AddressAutofillSuggestion) {
                showAddressAutofillSuggestion(
                    suggestion
                )
            }

            override fun onSuggestionsShown(suggestions: List<AddressAutofillSuggestion>) {}

            override fun onError(e: Exception) {}
        })

        binding.editTextNewOfferPostalAddress.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {

                val query = Query.create(text.toString())
                if (query != null) {
                    lifecycleScope.launchWhenStarted {
                        addressAutofillUiAdapter.search(query)
                    }
                }
                searchResultsView.isVisible = query != null
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {}
        })

    }

    private fun showAddressAutofillSuggestion(suggestion: AddressAutofillSuggestion) {
        val address = suggestion.result().address
        binding.editTextNewOfferCity.setText(address.place)
        binding.editTextNewOfferCountry.setText(address.country)
        binding.editTextNewOfferPostalAddress.setText(
            listOfNotNull(
                address.houseNumber,
                address.street
            ).joinToString()
        )
        binding.editTextNewOfferPostalAddress.clearFocus()

        jobOffer.address = suggestion.formattedAddress
        jobOffer.lat = suggestion.coordinate.latitude()
        jobOffer.lng = suggestion.coordinate.longitude()
        jobOffer.city = address.place
        jobOffer.country = address.country

        searchResultsView.isVisible = false
    }

    private fun fetchContractTypes(): io.reactivex.Observable<List<ContractType>> {
        return jobOfferViewModel
            .fetchAllContractTypes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()

    }

    private fun fetchProfessions(): io.reactivex.Observable<kotlin.collections.List<Profession>> {
        return jobOfferViewModel
            .fetchAllProfessions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
    }

    private fun fetchJobCategories(): io.reactivex.Observable<List<JobOfferCategory>> {
        return jobOfferViewModel
            .fetchAllJobCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}