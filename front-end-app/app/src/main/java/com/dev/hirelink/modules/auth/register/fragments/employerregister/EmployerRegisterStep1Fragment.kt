package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep1Binding
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.modules.auth.register.fragments.StepFragment
import com.mapbox.search.autofill.AddressAutofill
import com.mapbox.search.autofill.AddressAutofillSuggestion
import com.mapbox.search.autofill.Query
import com.mapbox.search.ui.adapter.autofill.AddressAutofillUiAdapter
import com.mapbox.search.ui.view.CommonSearchViewConfiguration
import com.mapbox.search.ui.view.DistanceUnitType
import com.mapbox.search.ui.view.SearchResultsView


class EmployerRegisterStep1Fragment : StepFragment() {
    private lateinit var binding: FragmentEmployerRegisterStep1Binding
    private val currentStep = RegistrationStep.STEP_1
    private lateinit var addressAutofill: AddressAutofill
    private lateinit var searchResultsView: SearchResultsView
    private lateinit var addressAutofillUiAdapter: AddressAutofillUiAdapter


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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonNext.setOnClickListener { createAccount() }
        attachTextWatchers()
        setupAddressAutofillField()
    }

    private fun createAccount() {
        listener.onNextButtonTouched(currentStep)
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

        addressAutofillUiAdapter.addSearchListener(object : AddressAutofillUiAdapter.SearchListener {

            override fun onSuggestionSelected(suggestion: AddressAutofillSuggestion) {
                showAddressAutofillSuggestion(
                    suggestion,
                    fromReverseGeocoding = false,
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

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }

            override fun afterTextChanged(s: Editable) { }
        })

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

    private fun showAddressAutofillSuggestion(suggestion: AddressAutofillSuggestion, fromReverseGeocoding: Boolean) {
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

        searchResultsView.isVisible = false
    }

}