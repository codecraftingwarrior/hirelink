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


class EmployerRegisterStep1Fragment : Fragment() {
    private lateinit var binding: FragmentEmployerRegisterStep1Binding
    private lateinit var listener: NextButtonClickListener
    private val currentStep = RegistrationStep.STEP_1

    interface NextButtonClickListener {
        fun onNextButtonTouched(step: RegistrationStep);
    }

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as NextButtonClickListener
        } catch (e: java.lang.Exception) {
            throw java.lang.Exception("$context must implements NextButtonClickListener")
        }
    }

}