package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.content.Context
import android.os.Bundle
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
        binding.buttonNext.setOnClickListener { listener.onNextButtonTouched(currentStep) }
    }

}