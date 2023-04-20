package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep1Binding
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.modules.auth.register.fragments.StepFragment


class EmployerRegisterStep1Fragment : StepFragment() {
    private lateinit var binding: FragmentEmployerRegisterStep1Binding
    private val currentStep = RegistrationStep.STEP_1

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
    }

    private fun createAccount() {
        listener.onNextButtonTouched(currentStep)
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

}