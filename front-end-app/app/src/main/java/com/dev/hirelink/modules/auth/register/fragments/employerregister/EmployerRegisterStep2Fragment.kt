package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep2Binding
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.modules.auth.register.fragments.StepFragment

class EmployerRegisterStep2Fragment : StepFragment() {
    private lateinit var binding: FragmentEmployerRegisterStep2Binding
    private val currentStep = RegistrationStep.STEP_2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employer_register_step2,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attachTextWatchers()

        bindListeners()
    }

    private fun attachTextWatchers() {
        with(binding) {
            editTextFirstDigit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (s?.length == 1)
                        editTextSecondDigit.requestFocus()

                    handleTextChange()
                }
            })

            editTextSecondDigit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (s?.length == 1)
                        editTextThirdDigit.requestFocus()
                    else if (s?.length == 0)
                        editTextFirstDigit.requestFocus()

                    handleTextChange()
                }
            })

            editTextThirdDigit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (s?.length == 1)
                        editTextFourthDigit.requestFocus()
                    else if (s?.length == 0)
                        editTextSecondDigit.requestFocus()

                    handleTextChange()
                }
            })

            editTextFourthDigit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (s?.length == 0)
                        editTextThirdDigit.requestFocus()

                    handleTextChange()
                }
            })
        }
    }

    private fun handleTextChange() {
        val firstDigit = binding.editTextFirstDigit.text.toString()
        val secondDigit = binding.editTextSecondDigit.text.toString()
        val thirdDigit = binding.editTextThirdDigit.text.toString()
        val fourthDigit = binding.editTextFourthDigit.text.toString()

        try {
            val digit = "$firstDigit$secondDigit$thirdDigit$fourthDigit".toInt()

            binding.buttonNext.isEnabled = digit in 1000..9999

        } catch (e: NumberFormatException) {
            binding.buttonNext.isEnabled = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindListeners() {
        binding.buttonNext.setOnClickListener { validateOTPDigit() }
    }

    private fun validateOTPDigit() {
        listener.onNextButtonTouched(currentStep)
    }
}