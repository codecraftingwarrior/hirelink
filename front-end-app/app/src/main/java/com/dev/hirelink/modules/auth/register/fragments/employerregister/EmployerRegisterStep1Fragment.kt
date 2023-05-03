package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.dev.hirelink.R
import com.dev.hirelink.components.HttpExceptionParser
import com.dev.hirelink.components.SharedPreferenceManager
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep1Binding
import com.dev.hirelink.dto.ApplicationUserRequest
import com.dev.hirelink.dto.BasicErrorResponse
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.enums.RoleType
import com.dev.hirelink.models.*
import com.dev.hirelink.modules.auth.register.RegisterActivity
import com.dev.hirelink.modules.auth.register.fragments.StepFragment
import com.dev.hirelink.modules.auth.register.fragments.candidateregister.RegisterViewModel
import com.dev.hirelink.modules.common.CustomLoadingOverlay
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
import retrofit2.HttpException


class EmployerRegisterStep1Fragment(private val roleType: RoleType) : StepFragment() {
    private lateinit var binding: FragmentEmployerRegisterStep1Binding
    private val currentStep = RegistrationStep.STEP_1
    private lateinit var addressAutofill: AddressAutofill
    private lateinit var searchResultsView: SearchResultsView
    private lateinit var addressAutofillUiAdapter: AddressAutofillUiAdapter
    private lateinit var sharedPrefs: SharedPreferenceManager
    private val company = Company()

    private val compositeDisposable = CompositeDisposable()
    private lateinit var customLoadingOverlay: CustomLoadingOverlay
    private lateinit var selectedRole: Role
    private lateinit var registerViewModel: RegisterViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employer_register_step1,
            container,
            false
        )
        binding.lifecycleOwner = this
        registerViewModel = (requireActivity() as RegisterActivity).registerViewModel
        sharedPrefs = SharedPreferenceManager(requireContext())
        checkStep()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonNext.setOnClickListener { createAccount() }
        customLoadingOverlay = CustomLoadingOverlay(
            requireContext(),
            R.id.card_step1,
            R.layout.loading_overlay_centered
        )
        attachTextWatchers()
        setupAddressAutofillField()
        fetchRole()
    }

    private fun fetchRole() {
        customLoadingOverlay.showLoading()
        val disposable = registerViewModel
            .roleRepository
            .findAll(code = listOf(roleType.code))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { customLoadingOverlay.hideLoading() }
            .subscribe(
                { roles -> this.selectedRole = roles[0] },
                { error: Throwable -> handleError(error) }
            )
        compositeDisposable.add(disposable)
    }

    private fun createAccount() {
        customLoadingOverlay.showLoading()
        company.name = binding.editTextCompanyName.text.toString()
        company.nationalUniqueNumber = binding.editTextNationalUniqueNumber.text.toString()
        company.mailAddress = binding.editTextEmail.text.toString()

        val applicationUser = ApplicationUserRequest(
            company = company,
            role = registerViewModel.roleRepository.toIRI(selectedRole),
            phoneNumber = binding.editTextPhoneNumber.text.toString(),
            email = binding.editTextEmail.text.toString()
        )

        val disposable = registerViewModel
            .register(applicationUser)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { customLoadingOverlay.hideLoading() }
            .subscribe(
                { user ->
                    Log.d(javaClass.simpleName, user.toString())
                    val bundle = bundleOf(
                        USER_ID_KEY to user?.id,
                        USER_EMAIL_KEY to user?.email,
                        NATIONAL_UNIQUE_NUMBER_KEY to company.nationalUniqueNumber
                    )
                    sharedPrefs.storeRegistrationAchievedStep(RegistrationStep.STEP_1)
                    listener.onNextButtonTouched(currentStep, bundle)
                },
                { error: Throwable -> handleError(error) }
            )
        compositeDisposable.add(disposable)
    }


    private fun setupAddressAutofillField() {
        addressAutofill = AddressAutofill.create(getString(R.string.mapbox_access_token))

        searchResultsView = binding.searchResultsView

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

        binding.editTextPostalAddress.addTextChangedListener(object : TextWatcher {

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

    private fun checkStep() {
        val currentRegistrationStep = sharedPrefs.getCurrentRegistrationStep()
        if (currentRegistrationStep == null || currentRegistrationStep.isEmpty())
            return

        if (currentRegistrationStep == RegistrationStep.STEP_1.name || RegistrationStep.valueOf(
                currentRegistrationStep
            ).number > RegistrationStep.STEP_1.number
        )
            listener.onNextButtonTouched(RegistrationStep.STEP_1)
    }

    private fun attachTextWatchers() {
        with(binding) {

            val watcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    buttonNext.isEnabled =
                        editTextCompanyName.text!!.isNotEmpty()
                                && editTextEmail.text!!.isNotEmpty()
                                && editTextPostalAddress.text!!.isNotEmpty()
                                && editTextCity.text!!.isNotEmpty()
                                && editTextCountry.text!!.isNotEmpty()
                }
            }

            editTextCompanyName.addTextChangedListener(watcher)
            editTextEmail.addTextChangedListener(watcher)
            editTextPostalAddress.addTextChangedListener(watcher)
            editTextCity.addTextChangedListener(watcher)
            editTextCountry.addTextChangedListener(watcher)
        }
    }

    private fun showAddressAutofillSuggestion(suggestion: AddressAutofillSuggestion) {
        val address = suggestion.result().address
        binding.editTextCity.setText(address.place)
        binding.editTextCountry.setText(address.country)
        binding.editTextPostalAddress.setText(
            listOfNotNull(
                address.houseNumber,
                address.street
            ).joinToString()
        )
        binding.editTextPostalAddress.clearFocus()

        company.address = suggestion.formattedAddress
        company.lat = suggestion.coordinate.latitude()
        company.lng = suggestion.coordinate.longitude()
        company.city = address.place
        company.country = address.country

        searchResultsView.isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun handleError(error: Throwable) {
        customLoadingOverlay.hideLoading()
        when (error) {
            is HttpException -> {
                try {
                    val errorResponse =
                        HttpExceptionParser.parse(error, BasicErrorResponse::class.java)
                    if (errorResponse.violations?.isEmpty() == false)
                        for (violation in errorResponse.violations)
                            Snackbar.make(
                                binding.buttonNext,
                                violation.message ?: "no message",
                                Snackbar.LENGTH_SHORT
                            ).show()

                    Log.d(javaClass.simpleName, error.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    Snackbar.make(
                        binding.buttonNext,
                        getString(R.string.error_msg),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            }
            else -> {
                error.printStackTrace()
                Snackbar.make(
                    binding.buttonNext,
                    getString(R.string.error_msg),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val USER_EMAIL_KEY = "userEmail"
        const val USER_ID_KEY = "userID"
        const val NATIONAL_UNIQUE_NUMBER_KEY = "nationalUniqueNumber"
    }
}