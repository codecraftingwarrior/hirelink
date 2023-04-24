package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep3Binding
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.modules.auth.register.fragments.StepFragment

class EmployerRegisterStep3Fragment : StepFragment() {
    private lateinit var binding: FragmentEmployerRegisterStep3Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employer_register_step3,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindListeners();
        attachTextWatchers()
    }

    private fun bindListeners() {
        binding.buttonNext.setOnClickListener { createPassword() }
    }

    private fun createPassword() {
        listener.onNextButtonTouched(RegistrationStep.STEP_3)
    }

    private fun attachTextWatchers() {
        with(binding) {

            val watcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    buttonNext.isEnabled =
                        editTextPassword.text!!.isNotEmpty()
                                && editTextPasswordConfirm.text!!.isNotEmpty()
                                && editTextPassword.text.toString() == editTextPasswordConfirm.text.toString()
                }
            }

            editTextPassword.addTextChangedListener(watcher)
            editTextPasswordConfirm.addTextChangedListener(watcher)
        }

    }
}