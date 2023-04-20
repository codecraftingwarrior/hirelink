package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep6CreditCardBinding
import com.dev.hirelink.enums.PaymentType
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.modules.auth.register.fragments.StepFragment
import com.vicmikhailau.maskededittext.MaskedFormatter

class EmployerRegisterStep6CreditCardFragment : StepFragment() {
    private lateinit var binding: FragmentEmployerRegisterStep6CreditCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employer_register_step6_credit_card,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNext.setOnClickListener { storePaymentInformations() }
        attachTextWatchers()
    }

    private fun storePaymentInformations() {
        listener.onNextButtonTouched(RegistrationStep.STEP_6_CREDIT_CARD)
    }

    private fun attachTextWatchers() {
        with(binding) {

            val watcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    buttonNext.isEnabled =
                        editTextAccountOwner.text!!.isNotEmpty()
                                && editTextCreditCardNumber.text!!.isNotEmpty()
                                && editTextExpDate.text!!.isNotEmpty()
                                && editTextCvc.text!!.isNotEmpty()
                                && Regex("\\d{4} \\d{4} \\d{4} \\d{4}").matches(editTextCreditCardNumber.text.toString())
                                && Regex("\\d{2}/\\d{2}").matches(editTextExpDate.text.toString())
                                && Regex("\\d{3}").matches(editTextCvc.text.toString())

                }
            }

            editTextAccountOwner.addTextChangedListener(watcher)
            editTextCreditCardNumber.addTextChangedListener(watcher)
            editTextExpDate.addTextChangedListener(watcher)
            editTextCvc.addTextChangedListener(watcher)
        }
    }

    companion object {
        val TAG = EmployerRegisterStep6CreditCardFragment::class.java.simpleName
    }
}