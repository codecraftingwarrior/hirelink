package com.dev.hirelink.modules.auth.register.fragments.employerregister

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import com.dev.hirelink.R
import com.dev.hirelink.databinding.FragmentEmployerRegisterStep5Binding
import com.dev.hirelink.enums.PaymentType
import com.dev.hirelink.enums.RegistrationStep
import com.dev.hirelink.modules.auth.register.fragments.StepFragment


class EmployerRegisterStep5Fragment : StepFragment() {
    private lateinit var binding: FragmentEmployerRegisterStep5Binding
    private lateinit var selectedPaymentType: PaymentType
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employer_register_step5,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindListener()
    }

    private fun bindListener() {
        binding.buttonNext.setOnClickListener {
            val bundle = bundleOf(
                "paymentType" to selectedPaymentType.name
            )
            listener.onNextButtonTouched(RegistrationStep.STEP_5, bundle)
        }

        binding.radioBtnCreditCard.setOnClickListener {
            binding.radioBtnDirectDebit.isChecked = false
            selectedPaymentType = PaymentType.CREDIT_CARD
        }

        binding.radioBtnDirectDebit.setOnClickListener {
            binding.radioBtnCreditCard.isChecked = false
            selectedPaymentType = PaymentType.DIRECT_DEBIT
        }
    }
}